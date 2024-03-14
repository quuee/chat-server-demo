//package com.demo.im.netty.processor;
//
//import cn.hutool.core.bean.BeanUtil;
//import com.demo.im.common.ChannelAttrKey;
//import com.demo.im.common.IMConstant;
//import com.demo.im.common.IMRedisKey;
//import com.demo.im.common.enums.IMCmdType;
//import com.demo.im.model.IMLoginInfo;
//import com.demo.im.model.IMMessageWrapper;
//import com.demo.im.netty.IMServersLaunch;
//import com.demo.im.netty.UserChannelCtxMap;
//import com.nimbusds.jose.JWSVerifier;
//import com.nimbusds.jwt.SignedJWT;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.util.AttributeKey;
//import lombok.RequiredArgsConstructor;
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Component;
//
//import java.util.HashMap;
//import java.util.concurrent.TimeUnit;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class LoginProcessor implements AbstractMessageProcessor<IMLoginInfo> {
//
//
//    private final RedisTemplate<String, Object> redisTemplate;
//
////    private final ObjectMapper objectMapper;
//
////    private final JWSSigner rsaSigner;
//
//    private final JWSVerifier rsaVerifier;
//
//
//    @Override
//    @SneakyThrows
//    public synchronized void process(ChannelHandlerContext ctx, IMLoginInfo loginInfo) {
//
//        SignedJWT parse = SignedJWT.parse(loginInfo.getAccessToken());
//        if(!parse.verify(rsaVerifier)){
//            // 验证不通过
//            ctx.channel().close();
//            log.warn("用户token校验不通过，强制下线,token:{}", loginInfo.getAccessToken());
//        }
//        String userIdStr = parse.getJWTClaimsSet().getSubject();
//        Object terminalObj = parse.getJWTClaimsSet().getClaim("terminal");
//
//        Long userId = Long.parseLong(userIdStr);
//        Integer terminal = Integer.parseInt(String.valueOf(terminalObj));
//
//        log.info("用户登录，userId:{}", userId);
//        ChannelHandlerContext context = UserChannelCtxMap.getChannelCtx(userId, terminal);
//        if (context != null && !ctx.channel().id().equals(context.channel().id())) {
//            // 不允许多地登录,强制下线
//            IMMessageWrapper<Object> sendInfo = new IMMessageWrapper<>();
//            sendInfo.setCmd(IMCmdType.FORCE_LOGOUT.code());
//            sendInfo.setData("您已在其他地方登陆，将被强制下线");
//            context.channel().writeAndFlush(sendInfo);
//            log.info("异地登录，强制下线,userId:{}", userId);
//        }
//        // 绑定用户和channel
//        UserChannelCtxMap.addChannelCtx(userId, terminal, ctx);
//        // 设置用户id属性
//        AttributeKey<Long> userIdAttr = AttributeKey.valueOf(ChannelAttrKey.USER_ID);
//        ctx.channel().attr(userIdAttr).set(userId);
//        // 设置用户终端类型
//        AttributeKey<Integer> terminalAttr = AttributeKey.valueOf(ChannelAttrKey.TERMINAL_TYPE);
//        ctx.channel().attr(terminalAttr).set(terminal);
//        // 初始化心跳次数
//        AttributeKey<Long> heartBeatAttr = AttributeKey.valueOf(ChannelAttrKey.HEARTBEAT_TIMES);
//        ctx.channel().attr(heartBeatAttr).set(0L);
//        // 在redis上记录每个user的channelId，15秒没有心跳，则自动过期
//        String key = String.join(":", IMRedisKey.IM_USER_SERVER_ID, userId.toString(), terminal.toString());
//        redisTemplate.opsForValue().set(key, IMServersLaunch.machineId, IMConstant.ONLINE_TIMEOUT_SECOND, TimeUnit.SECONDS);
//        // 响应ws
//        IMMessageWrapper<Object> sendInfo = new IMMessageWrapper<>();
//        sendInfo.setCmd(IMCmdType.LOGIN.code());
//        ctx.channel().writeAndFlush(sendInfo);
//    }
//
//
//    @Override
//    public IMLoginInfo transForm(Object o) {
//        HashMap map = (HashMap) o;
//        return BeanUtil.fillBeanWithMap(map, new IMLoginInfo(), false);
//    }
//}
