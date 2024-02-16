package com.demo.im.netty.processor;


import cn.hutool.core.bean.BeanUtil;
import com.demo.im.common.enums.IMConversationType;
import com.demo.im.model.IMMessageInfo;
import com.demo.im.model.IMMessageWrapper;
import com.demo.im.model.IMUserInfo;
import com.demo.im.netty.UserChannelCtxMap;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;


@Slf4j
@Component
@RequiredArgsConstructor
public class GroupMessageProcessor extends AbstractMessageProcessor<IMMessageInfo> {

    private final RedisTemplate<String, Object> redisTemplate;


    @Override
    public void process(ChannelHandlerContext ctx, IMMessageInfo recvInfo) {
        process(recvInfo);
    }

    public void process(IMMessageInfo recvInfo){
        IMUserInfo sender = recvInfo.getSender();
        List<IMUserInfo> receivers = recvInfo.getReceivers();
        log.info("接收到群消息，发送者:{},接收用户数量:{}，内容:{}", sender.getUserId(), receivers.size(), recvInfo.getContent());
        for (IMUserInfo receiver : receivers) {
            try {
                ChannelHandlerContext channelCtx = UserChannelCtxMap.getChannelCtx(receiver.getUserId(), receiver.getTerminal());
                if (channelCtx != null) {
                    // 推送消息到用户
                    IMMessageWrapper sendInfo = new IMMessageWrapper();
                    sendInfo.setConversationType(IMConversationType.GROUP_MESSAGE.code());
                    sendInfo.setData(recvInfo);
                    channelCtx.channel().writeAndFlush(sendInfo);
                    // 消息发送成功确认
//                    sendSuccessResult(recvInfo, receiver, IMSendCode.SUCCESS);

                } else {
                    // 消息发送成功确认
//                    sendSuccessResult(recvInfo, receiver, IMSendCode.NOT_FIND_CHANNEL);
                    log.error("未找到channel,发送者:{},接收id:{}，内容:{}", sender.getUserId(), receiver.getUserId(), recvInfo.getContent());
                }
            } catch (Exception e) {
                // 消息发送失败确认
//                sendSuccessResult(recvInfo, receiver, IMSendCode.UNKNOWN_ERROR);
                log.error("发送消息异常,发送者:{},接收id:{}，内容:{}", sender.getUserId(), receiver.getUserId(), recvInfo.getContent());
            }
        }
    }

    @Override
    public IMMessageInfo transForm(Object o) {
        HashMap map = (HashMap) o;
        return BeanUtil.fillBeanWithMap(map, new IMMessageInfo(), false);
    }
}
