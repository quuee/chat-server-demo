package com.demo.im.netty.processor;

import cn.hutool.core.bean.BeanUtil;
import com.demo.im.common.ChannelAttrKey;
import com.demo.im.common.IMConstant;
import com.demo.im.common.IMRedisKey;
import com.demo.im.common.enums.IMCmdType;
import com.demo.im.model.IMMessageWrapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class HeartbeatProcessor extends AbstractMessageProcessor<IMMessageWrapper> {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void process(ChannelHandlerContext ctx, IMMessageWrapper beatInfo) {
        log.info("接收心跳");
        // 响应ws
        IMMessageWrapper sendInfo = new IMMessageWrapper();
        sendInfo.setCmd(IMCmdType.HEART_BEAT.code());
        ctx.channel().writeAndFlush(sendInfo);

        // 设置属性 (app后台 连接断开，重新发送的心跳，没有经过loginProcessor，定义统一token handler IMAuthHandler)
        AttributeKey<Long> heartBeatAttr = AttributeKey.valueOf(ChannelAttrKey.HEARTBEAT_TIMES);
        Long heartbeatTimes = ctx.channel().attr(heartBeatAttr).get();
        ctx.channel().attr(heartBeatAttr).set(++heartbeatTimes);
        if (heartbeatTimes % 10 == 0) {
            // 每心跳10次，用户在线状态续一次命 (客户端每30秒发送一次，总时间不能超过设定的时间)
            AttributeKey<Long> userIdAttr = AttributeKey.valueOf(ChannelAttrKey.USER_ID);
            Long userId = ctx.channel().attr(userIdAttr).get();
            AttributeKey<Integer> terminalAttr = AttributeKey.valueOf(ChannelAttrKey.TERMINAL_TYPE);
            Integer terminal = ctx.channel().attr(terminalAttr).get();
            String key = String.join(":", IMRedisKey.IM_USER_SERVER_ID, userId.toString(), terminal.toString());
            redisTemplate.expire(key, IMConstant.ONLINE_TIMEOUT_SECOND, TimeUnit.SECONDS);
        }
    }

    @Override
    public IMMessageWrapper transForm(Object o) {
        HashMap map = (HashMap) o;
        return BeanUtil.fillBeanWithMap(map, new IMMessageWrapper(), false);
    }
}
