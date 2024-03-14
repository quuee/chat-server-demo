package cn.quuee.chatServer.netty;

import cn.quuee.chatServer.common.ChannelAttrKey;
import cn.quuee.chatServer.common.IMConstant;
import cn.quuee.chatServer.common.enums.IMConversationType;
import cn.quuee.chatServer.util.RedisStreamUtil;
import cn.quuee.chatServer.common.IMRedisKey;
import cn.quuee.chatServer.config.redisConfig.RedisMqProperties;
import cn.quuee.chatServer.model.IMMessageWrapper;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jwt.SignedJWT;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * token校验handler
 * @ChannelHandler.Sharable
 *   1、如果每次通过new 而不是共享的方式，那么加不加@Sharable 效果都是一样的。每个Channel使用不通的ChannelHandler 对象。
 *   2、如果通过共享的方式，也就是 Handler 实例只有一个，那么必须要加@Sharable ，表明它是可以共享的，否则 第二次建立连接的时候会报错
 */
@ChannelHandler.Sharable
@Component
@Slf4j
public class IMAuthHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private JWSVerifier rsaVerifier;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisStreamUtil redisStreamUtil;

    @Autowired
    private RedisMqProperties redisMqProperties;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            // 获取请求头中的身份验证令牌
            FullHttpRequest request = (FullHttpRequest) msg;
            HttpHeaders headers = request.headers();
            if (headers.isEmpty()) {
                ctx.channel().close();
                return;
            }
            String token = headers.get("Sec-WebSocket-Protocol");
            log.debug("Authentication success. uid: {}", token);

            SignedJWT parse = SignedJWT.parse(token);
            if(!parse.verify(rsaVerifier)){
                // 验证不通过
                ctx.channel().close();
                log.warn("用户token校验不通过，强制下线,token:{}", token);
            }

            String userIdStr = parse.getJWTClaimsSet().getSubject();
            Object terminalObj = parse.getJWTClaimsSet().getClaim("terminal");
            Long userId = Long.parseLong(userIdStr);
            Integer terminal = Integer.parseInt(String.valueOf(terminalObj));

            ChannelHandlerContext context = UserChannelCtxMap.getChannelCtx(userId, terminal);
            if (context != null && !ctx.channel().id().equals(context.channel().id())) {
                // 不允许多地登录,强制下线
                IMMessageWrapper<Object> sendInfo = new IMMessageWrapper<>();
                sendInfo.setConversationType(IMConversationType.FORCE_LOGOUT.code());
                sendInfo.setData("您已在其他地方登陆，将被强制下线");
                context.channel().writeAndFlush(sendInfo);
                log.info("异地登录，强制下线,userId:{}", userId);
            }
            // 绑定用户和channel
            UserChannelCtxMap.addChannelCtx(userId, terminal, ctx);
            // 设置用户id属性
            AttributeKey<Long> userIdAttr = AttributeKey.valueOf(ChannelAttrKey.USER_ID);
            ctx.channel().attr(userIdAttr).set(userId);
            // 设置用户终端类型
            AttributeKey<Integer> terminalAttr = AttributeKey.valueOf(ChannelAttrKey.TERMINAL_TYPE);
            ctx.channel().attr(terminalAttr).set(terminal);
            // 初始化心跳次数
            AttributeKey<Long> heartBeatAttr = AttributeKey.valueOf(ChannelAttrKey.HEARTBEAT_TIMES);
            ctx.channel().attr(heartBeatAttr).set(0L);

            // 在redis上记录每个user的channelId，n秒没有心跳，则自动过期
            String key = String.join(":", IMRedisKey.IM_USER_SERVER_ID, userId.toString(), terminal.toString());
            redisTemplate.opsForValue().set(key, IMServersLaunch.serverId, IMConstant.ONLINE_TIMEOUT_SECOND, TimeUnit.SECONDS);

            // 该连接去除验证handler
            ctx.pipeline().remove(this);

            // 响应ws
            IMMessageWrapper<Object> sendInfo = new IMMessageWrapper<>();
            sendInfo.setConversationType(IMConversationType.HEART_BEAT.code());
            ctx.channel().writeAndFlush(sendInfo);

            // 发送用户登录通知，推送未读消息
            // 判断是否有未读消息
            String unreadMessageKey = String.join(":", IMRedisKey.IM_MESSAGE_PRIVATE_UNREAD_QUEUE,userId.toString(), terminal.toString());
            if(redisTemplate.hasKey(unreadMessageKey)){
                // redisMq.getStreams().get(0).getName() = key name IM:UNREAD:PUSH:PRIVATE
                redisStreamUtil.addMap(redisMqProperties.getStreams().get(0).getName(), Map.of(IMRedisKey.IM_USER,String.join(":",userId.toString(),terminal.toString())));
            }


            // 传递消息至下一个处理器
            ctx.fireChannelRead(msg);
        } else {
            ctx.channel().close();
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("exceptionCaught:{}",cause.getMessage());
        ctx.channel().close();
    }

}
