package cn.quuee.chatServer.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamInfo;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RedisStreamUtil {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 创建消费组
     * @param key stream-key值
     * @param group 消费组
     * @return java.lang.String
     */
    public String createGroup(String key, String group){
        return stringRedisTemplate.opsForStream().createGroup(key, group);
    }

    /**
     * 获取消费者信息
     * @param key stream-key值
     * @param group 消费组
     * @return org.springframework.data.redis.connection.stream.StreamInfo.XInfoConsumers
     */
    public StreamInfo.XInfoConsumers queryConsumers(String key, String group){
        return stringRedisTemplate.opsForStream().consumers(key, group);
    }

    /**
     * 添加Map消息
     * @param key stream对应的key
     * @param value 消息数据
     * @return
     */
    public String addMap(String key, Map<String, Object> value){
        return stringRedisTemplate.opsForStream().add(key, value).getValue();
    }

    /**
     * 读取消息
     * @param: key
     * @return java.util.List<org.springframework.data.redis.connection.stream.MapRecord<java.lang.String,java.lang.Object,java.lang.Object>>
     */
    public List<MapRecord<String, Object, Object>> read(String key){
        return stringRedisTemplate.opsForStream().read(StreamOffset.fromStart(key));
    }

    /**
     * 从指定的ID开始读
     * @param key
     * @param recordId
     * @return
     */
    public List<MapRecord<String, Object, Object>> read(String key, String recordId){
        return stringRedisTemplate.opsForStream().read(StreamOffset.from(MapRecord.create(key, new HashMap<>(1)).withId(RecordId.of(recordId))));
    }

    /**
     * 确认消费
     * @param key
     * @param group
     * @param recordIds
     * @return java.lang.Long
     */
    public Long ack(String key, String group, String... recordIds){
        return stringRedisTemplate.opsForStream().acknowledge(key, group, recordIds);
    }



    /**
     * 删除消息。当一个节点的所有消息都被删除，那么该节点会自动销毁
     * @param: key
     * @param: recordIds
     * @return java.lang.Long
     */
    public Long del(String key, String... recordIds){
        return stringRedisTemplate.opsForStream().delete(key, recordIds);
    }

    /**
     * 判断是否存在key
     * @param key
     * @return
     */
    public boolean hasKey(String key) {
        Boolean aBoolean = stringRedisTemplate.hasKey(key);
        return aBoolean == null ? false : aBoolean;
    }

    /**
     * 消息长度
     * @param key
     * @return
     */
    public Long len(String key){
        return stringRedisTemplate.opsForStream().size(key);
    }

}
