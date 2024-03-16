package cn.quuee.chatServer.netty.processor;

import cn.hutool.core.bean.BeanUtil;
import cn.quuee.chatServer.common.enums.IMConversationType;
import cn.quuee.chatServer.common.enums.IMClientType;
import cn.quuee.chatServer.model.IMMessageInfo;
import cn.quuee.chatServer.model.IMMessageWrapper;
import cn.quuee.chatServer.model.IMUserInfo;
import cn.quuee.chatServer.common.IMRedisKey;
import cn.quuee.chatServer.config.minioConfig.MinioProperties;
import com.demo.im.model.*;
import cn.quuee.chatServer.netty.UserChannelCtxMap;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class PrivateMessageProcessor extends AbstractMessageProcessor<IMMessageInfo> {

    private final RedisTemplate<String, Object> redisTemplate;

    private final MinioProperties minioProperties;

    @Override
    public void process(ChannelHandlerContext ctx, IMMessageInfo recvInfo) {
        process(recvInfo);
    }

    public void process(IMMessageInfo receiveInfo) {
        log.info("接收到私聊消息，{}", receiveInfo);
        IMUserInfo sender = receiveInfo.getSender();
        IMUserInfo receiver = receiveInfo.getReceivers().get(0);

        try {
            ChannelHandlerContext channelCtx = UserChannelCtxMap.getChannelCtx(receiver.getUserId(), receiver.getTerminal());
            if (channelCtx != null) {

                // 判断是不是好友关系

                // 推送消息到目标用户
                IMMessageWrapper<Object> resultInfo = new IMMessageWrapper<>();
                resultInfo.setConversationType(IMConversationType.PRIVATE_MESSAGE.code());
                resultInfo.setData(receiveInfo);

                channelCtx.channel().writeAndFlush(resultInfo);
            } else {
                log.error("未找到channel，发送者:{},接收者:{}，内容:{}", sender.getUserId(), receiver.getUserId(), receiveInfo.getContent());
                // 如果对方不在线，将消息存入缓存或数据库中，等下次对方连接服务器，将消息推送
                pushUnReadQueue(receiveInfo);
            }
        } catch (Exception e) {
            // 消息推送失败确认
            log.error("发送异常，发送者:{},接收者:{}，内容:{}", sender.getUserId(), receiver.getUserId(), receiveInfo.getContent(), e);

        }
    }

    private void pushUnReadQueue(IMMessageInfo recvInfo) {
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
        String sendKey = String.join(":", IMRedisKey.IM_MESSAGE_PRIVATE_UNREAD_QUEUE,recvInfo.getReceivers().get(0).getUserId().toString(), IMClientType.APP.code().toString());
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
