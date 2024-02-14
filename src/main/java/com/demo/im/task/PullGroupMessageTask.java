//package com.demo.im.task;
//
//
//import cn.hutool.json.JSONObject;
//import com.demo.im.model.IMReceiveInfo;
//import com.demo.im.netty.IMServersLaunch;
//import com.demo.im.netty.processor.AbstractMessageProcessor;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Component;
//
//import java.util.Objects;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class PullGroupMessageTask extends AbstractPullMessageTask {
//
//    private final RedisTemplate<String, Object> redisTemplate;
//
//    @Override
//    public void pullMessage() {
//        // 从redis拉取未读消息
//        String key = String.join(":", IMRedisKey.IM_MESSAGE_GROUP_QUEUE, IMServersLaunch.serverId + "");
//        JSONObject jsonObject = (JSONObject) redisTemplate.opsForList().leftPop(key);
//        while (!Objects.isNull(jsonObject)) {
//            IMReceiveInfo recvInfo = jsonObject.toJavaObject(IMRecvInfo.class);
//            AbstractMessageProcessor processor = ProcessorFactory.createProcessor(IMCmdType.GROUP_MESSAGE);
//            processor.process(recvInfo);
//            // 下一条消息
//            jsonObject = (JSONObject) redisTemplate.opsForList().leftPop(key);
//        }
//    }
//
//}
