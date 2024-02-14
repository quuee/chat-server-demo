package com.demo.im.netty.processor;

import cn.hutool.core.bean.BeanUtil;
import com.demo.im.common.IMRedisKey;
import com.demo.im.common.enums.IMCmdType;
import com.demo.im.common.enums.IMTerminalType;
import com.demo.im.model.*;
import com.demo.im.netty.UserChannelCtxMap;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class PrivateMessageProcessor extends AbstractMessageProcessor<IMMessageInfo> {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void process(ChannelHandlerContext ctx, IMMessageInfo recvInfo) {
        process(recvInfo);
    }

    public void process(IMMessageInfo receiveInfo){
        log.info("接收到私聊消息，{}", receiveInfo);
        IMUserInfo sender = receiveInfo.getSender();
        IMUserInfo receiver = receiveInfo.getReceivers().get(0);

        try {
            ChannelHandlerContext channelCtx = UserChannelCtxMap.getChannelCtx(receiver.getUserId(), receiver.getTerminal());
            if (channelCtx != null) {

                // 判断是不是好友关系

                // 推送消息到目标用户
                IMMessageWrapper<Object> resultInfo = new IMMessageWrapper<>();
                resultInfo.setCmd(IMCmdType.PRIVATE_MESSAGE.code());
                resultInfo.setData(receiveInfo);

                channelCtx.channel().writeAndFlush(resultInfo);
            } else {
                log.error("未找到channel，发送者:{},接收者:{}，内容:{}", sender.getUserId(), receiver.getUserId(), receiveInfo.getData());
                // 如果对方不在线，将消息存入缓存或数据库中，等下次对方连接服务器，将消息推送
                sendUnReadQueue(receiveInfo);
            }
        } catch (Exception e) {
            // 消息推送失败确认
            log.error("发送异常，发送者:{},接收者:{}，内容:{}", sender.getUserId(), receiver.getUserId(), receiveInfo.getData(), e);

        }
    }

    private void sendUnReadQueue(IMMessageInfo recvInfo){
        // 暂时发一个终端

        String sendKey = String.join(":", IMRedisKey.IM_MESSAGE_PRIVATE_UNREAD_QUEUE);
        // 存入redis 等待拉取推送
        redisTemplate.opsForList().rightPush(sendKey, recvInfo);
    }

    @Override
    public IMMessageInfo transForm(Object o) {
        HashMap map = (HashMap) o;
        return BeanUtil.fillBeanWithMap(map, new IMMessageInfo(), false);
    }
}
