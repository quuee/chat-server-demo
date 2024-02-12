package com.demo.im.netty.processor;

import cn.hutool.core.bean.BeanUtil;
import com.demo.im.common.IMRedisKey;
import com.demo.im.common.enums.IMCmdType;
import com.demo.im.common.enums.IMSendCode;
import com.demo.im.model.*;
import com.demo.im.netty.UserChannelCtxMap;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class PrivateMessageProcessor implements AbstractMessageProcessor<IMRecvInfo> {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void process(ChannelHandlerContext ctx,IMRecvInfo recvInfo) {
        log.info("接收到私聊消息，{}", recvInfo);
        IMUserInfo sender = recvInfo.getSender();
        IMUserInfo receiver = recvInfo.getReceivers().get(0);

        try {
            ChannelHandlerContext channelCtx = UserChannelCtxMap.getChannelCtx(receiver.getUserId(), receiver.getTerminal());
            if (channelCtx != null) {
                // 推送消息到目标用户
                IMMessageWrapper<Object> resultInfo = new IMMessageWrapper<>();
                resultInfo.setCmd(IMCmdType.PRIVATE_MESSAGE.code());

                IMSendResult<Object> result = new IMSendResult<>();
                result.setSender(sender);
                result.setReceiver(receiver);
                result.setCmd(IMCmdType.PRIVATE_MESSAGE.code());
                result.setData(recvInfo.getData());

                resultInfo.setData(result);
                channelCtx.channel().writeAndFlush(resultInfo);
                // 消息发送成功确认
                sendResult(recvInfo, IMSendCode.SUCCESS);
            } else {
                // 消息推送失败确认
                sendResult(recvInfo, IMSendCode.NOT_FIND_CHANNEL);
                log.error("未找到channel，发送者:{},接收者:{}，内容:{}", sender.getUserId(), receiver.getUserId(), recvInfo.getData());
            }
        } catch (Exception e) {
            // 消息推送失败确认
            sendResult(recvInfo, IMSendCode.UNKNOWN_ERROR);
            log.error("发送异常，发送者:{},接收者:{}，内容:{}", sender.getUserId(), receiver.getUserId(), recvInfo.getData(), e);
        }

    }

    private void sendResult(IMRecvInfo recvInfo, IMSendCode sendCode) {
        // 是否需要存入redis
        if (recvInfo.getSendResult()) {
            IMSendResult<Object> result = new IMSendResult<>();
            result.setSender(recvInfo.getSender());
            result.setReceiver(recvInfo.getReceivers().get(0));
            result.setCmd(sendCode.code());
            result.setData(recvInfo.getData());
            // 推送到结果队列
            String key = String.join(":", IMRedisKey.IM_RESULT_PRIVATE_QUEUE,recvInfo.getServiceName());
            redisTemplate.opsForList().rightPush(key, result);
        }
    }

    @Override
    public IMRecvInfo transForm(Object o) {
        HashMap map = (HashMap) o;
        return BeanUtil.fillBeanWithMap(map, new IMRecvInfo(), false);
    }
}
