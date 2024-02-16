package com.demo.im.task;

import com.demo.im.common.IMRedisKey;
import com.demo.im.common.enums.IMConversationType;
import com.demo.im.config.redisConfig.RedisMq;
import com.demo.im.model.IMMessageInfo;
import com.demo.im.netty.processor.AbstractMessageProcessor;
import com.demo.im.netty.processor.ProcessorFactory;
import com.demo.im.util.RedisStreamUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * redis stream监听消息
 */
@Slf4j
@Component
public class RedisListenerMessageListener implements StreamListener<String, MapRecord<String,String,String>> {

    @Autowired
    private RedisMq redisMq;
    @Autowired
    private RedisStreamUtil redisStreamUtil;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onMessage(MapRecord<String, String, String> message) {
        // stream的key值
        String streamKey = message.getStream();
        // 消息ID
        RecordId recordId = message.getId();
        // 消息内容
        Map<String, String> msg = message.getValue();

        log.info("streamKey:{},recordId:{},msg:{}",streamKey,recordId,msg);
        String userString = msg.get(IMRedisKey.IM_USER);
        String[] split = userString.split(":");// userId:terminal
        // 逻辑
        String key = String.join(":", IMRedisKey.IM_MESSAGE_PRIVATE_UNREAD_QUEUE,split[0], split[1]);
        LinkedHashMap messageMap = (LinkedHashMap) redisTemplate.opsForList().leftPop(key);
        if(!ObjectUtils.isEmpty(messageMap)){
            IMMessageInfo receiveInfo = objectMapper.convertValue(messageMap, IMMessageInfo.class);
            log.info("拉取消息 推送到客户端，发送者:{},接收者:{}，内容:{}", receiveInfo.getSender().getUserId(), receiveInfo.getReceivers().get(0).getUserId(), receiveInfo.getContent());
            AbstractMessageProcessor processor = ProcessorFactory.createProcessor(IMConversationType.PRIVATE_MESSAGE);
            processor.process(receiveInfo);

            // ack
            redisStreamUtil.ack(streamKey,redisMq.getStreams().get(0).getGroups().get(0).getName(),recordId.getValue());
            redisStreamUtil.del(streamKey,recordId.getValue());
        }


    }

//    @Override
//    public void onMessage(Message message, byte[] pattern) {
//
//    }
}
