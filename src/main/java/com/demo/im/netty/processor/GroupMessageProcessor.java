package com.demo.im.netty.processor;


import cn.hutool.core.bean.BeanUtil;
import com.demo.im.common.IMRedisKey;
import com.demo.im.common.enums.IMConversationType;
import com.demo.im.common.enums.IMTerminalType;
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
                    IMMessageWrapper resultInfo = new IMMessageWrapper();
                    resultInfo.setConversationType(IMConversationType.GROUP_MESSAGE.code());
                    resultInfo.setData(recvInfo);

                    channelCtx.channel().writeAndFlush(resultInfo);

                } else {

                    log.error("未找到channel,发送者:{},接收id:{}，内容:{}", sender.getUserId(), receiver.getUserId(), recvInfo.getContent());
                    // 如果对方不在线，将消息存入缓存或数据库中，等下次对方连接服务器，将消息推送
//                    pushUnReadQueue(recvInfo,receiver.getUserId());
                }
            } catch (Exception e) {

                log.error("发送消息异常,发送者:{},接收id:{}，内容:{}", sender.getUserId(), receiver.getUserId(), recvInfo.getContent());
            }
        }
    }

    private void pushUnReadQueue(IMMessageInfo recvInfo,Long offlineUserId) {
        // 暂时只APP端

        // 获取对方连接的(所在的服务器severId)
        // 但是对方都没有上线，根本无法获取
//        String key = String.join(":", IMRedisKey.IM_USER_SERVER_ID, recvInfo.getReceivers().get(0).getUserId().toString(), IMTerminalType.APP.code().toString());
        // 注意，必须先启动服务，再注册客户端。
        // 每个客户端连接不同的服务器，需要找到对应的服务器获取channel 才能发送到达。
//        Integer serverId = (Integer) redisTemplate.opsForValue().get(key);
//        if (!ObjectUtils.isEmpty(serverId)) {
//            String sendKey = String.join(":", IMRedisKey.IM_MESSAGE_PRIVATE_UNREAD_QUEUE, serverId.toString());
//            // 存入redis 等待拉取推送
//            redisTemplate.opsForList().rightPush(sendKey, recvInfo);
//        }

        // 思路1：按用户id为key存储未读消息，当用户登录时 用消息中间件 通知用户所在的服务器推送消息
        String sendKey = String.join(":", IMRedisKey.IM_MESSAGE_GROUP_UNREAD_QUEUE,offlineUserId.toString(), IMTerminalType.APP.code().toString());
        // 存入redis 等待拉取推送
        redisTemplate.opsForList().rightPush(sendKey, recvInfo);
        // 如果后续消息太多存入数据库
    }

    @Override
    public IMMessageInfo transForm(Object o) {
        HashMap map = (HashMap) o;
        return BeanUtil.fillBeanWithMap(map, new IMMessageInfo(), false);
    }
}
