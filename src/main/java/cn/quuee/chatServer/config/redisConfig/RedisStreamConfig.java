package cn.quuee.chatServer.config.redisConfig;

import cn.quuee.chatServer.util.RedisStreamUtil;
import cn.quuee.chatServer.util.ThreadPoolExecutorFactory;
import cn.quuee.chatServer.task.RedisListenerPrivateMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.Subscription;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Configuration
public class RedisStreamConfig {

    @Autowired
    private RedisStreamUtil redisStreamUtil;
    @Autowired
    private RedisMqProperties redisMqProperties;
    @Autowired
    private RedisListenerPrivateMessageListener redisListenerPrivateMessageListener;

    @Bean
    public List<Subscription> subscription(LettuceConnectionFactory factory) {
        List<Subscription> resultList = new ArrayList<>();
//        AtomicInteger index = new AtomicInteger(1);
//        int processors = Runtime.getRuntime().availableProcessors();
//        ThreadPoolExecutor executor = new ThreadPoolExecutor(processors, processors, 0, TimeUnit.SECONDS,
//                new LinkedBlockingDeque<>(), r -> {
//            Thread thread = new Thread(r);
//            thread.setName("async-stream-consumer-" + index.getAndIncrement());
//            thread.setDaemon(true);
//            return thread;
//        });
        ThreadPoolExecutor executor = ThreadPoolExecutorFactory.getThreadPoolExecutor();

        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, MapRecord<String, String, String>> options =
                StreamMessageListenerContainer
                        .StreamMessageListenerContainerOptions
                        .builder()
                        // 一次最多获取多少条消息
                        .batchSize(10)
                        .executor(executor)
                        .pollTimeout(Duration.ofSeconds(1))////阻塞式轮询
                        // .errorHandler()
                        .build();

        for (RedisMqStreamProperties redisMqStreamProperties : redisMqProperties.getStreams()) {
            String streamName = redisMqStreamProperties.getName();
            RedisMqGroupProperties redisMqGroupProperties = redisMqStreamProperties.getGroups().get(0);

            initStream(streamName, redisMqGroupProperties.getName());

            var listenerContainer = StreamMessageListenerContainer.create(factory, options);
            // 手动ask消息
            Subscription subscription = listenerContainer.receive(
                    Consumer.from(redisMqGroupProperties.getName(), redisMqGroupProperties.getConsumers()[0]),
                    StreamOffset.create(streamName, ReadOffset.lastConsumed()),
                    redisListenerPrivateMessageListener);
            // 自动ask消息
            /*
           Subscription subscription = listenerContainer.receiveAutoAck(Consumer.from(redisMqGroup.getName(), redisMqGroup.getConsumers()[0]),
                    StreamOffset.create(streamName, ReadOffset.lastConsumed()), new ReportReadMqListener());
             */

            resultList.add(subscription);
            listenerContainer.start();
        }
        return resultList;
    }

    private void initStream(String key, String group) {
        boolean hasKey = redisStreamUtil.hasKey(key);
        if (!hasKey) {
            Map<String, Object> map = new HashMap<>(1);
            map.put("field", "value");
            //创建主题
            String result = redisStreamUtil.addMap(key, map);
            //创建消费组
            redisStreamUtil.createGroup(key, group);
            //将初始化的值删除掉
            redisStreamUtil.del(key, result);
            log.info("stream:{}-group:{} initialize success", key, group);
        }
    }
}
