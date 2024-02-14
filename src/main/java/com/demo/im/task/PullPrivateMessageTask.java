package com.demo.im.task;


import com.demo.im.common.IMRedisKey;
import com.demo.im.common.enums.IMCmdType;
import com.demo.im.model.IMMessageInfo;
import com.demo.im.netty.IMServersLaunch;
import com.demo.im.netty.processor.AbstractMessageProcessor;
import com.demo.im.netty.processor.ProcessorFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class PullPrivateMessageTask extends AbstractPullMessageTask {

    private final RedisTemplate<String, Object> redisTemplate;

    private final ObjectMapper objectMapper;

    @Override
    public void pullMessage() {
        // 从redis拉取未读消息
        String key = String.join(":", IMRedisKey.IM_MESSAGE_PRIVATE_UNREAD_QUEUE);
        LinkedHashMap messageMap = (LinkedHashMap) redisTemplate.opsForList().leftPop(key);
        while (!Objects.isNull(messageMap)) {
            IMMessageInfo receiveInfo = objectMapper.convertValue(messageMap, IMMessageInfo.class);
            log.debug("拉取消息 推送到客户端，发送者:{},接收者:{}，内容:{}", receiveInfo.getSender().getUserId(), receiveInfo.getReceivers().get(0).getUserId(), receiveInfo.getData());
            AbstractMessageProcessor processor = ProcessorFactory.createProcessor(IMCmdType.PRIVATE_MESSAGE);
            processor.process(receiveInfo);
            // 下一条消息
            messageMap = (LinkedHashMap) redisTemplate.opsForList().leftPop(key);
        }
    }
}
