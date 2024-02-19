package com.demo.im.netty;

import com.demo.im.common.ChannelAttrKey;
import com.demo.im.common.IMRedisKey;
import com.demo.im.common.enums.IMConversationType;
import com.demo.im.model.IMMessageWrapper;
import com.demo.im.netty.processor.AbstractMessageProcessor;
import com.demo.im.netty.processor.ProcessorFactory;
import com.demo.im.util.SpringContextHolder;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


/**
 * WebSocket 长连接下 文本帧的处理器
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class IMEntryPointChannelHandler extends SimpleChannelInboundHandler<IMMessageWrapper> {



    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, IMMessageWrapper imMessageWrapper) throws Exception {
        log.info("IMEntryPointChannelHandler 接收的内容：{}", imMessageWrapper);
        // 创建处理器进行处理
        AbstractMessageProcessor processor = ProcessorFactory.createProcessor(IMConversationType.fromCode(imMessageWrapper.getConversationType()));
        processor.process(channelHandlerContext,processor.transForm(imMessageWrapper.getData()));

    }

    /**
     * 出现异常的处理 打印报错日志
     *
     * @param ctx   channel上下文
     * @param cause 异常信息
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("exceptionCaught:{}",cause.getMessage());
        //关闭上下文
        //ctx.close();
    }

    /**
     * 监控浏览器上线
     *
     * @param ctx channel上下文
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        log.info("监控连接，ID：{}",ctx.channel().id().asLongText());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        // 定义一个AttributeKey
        AttributeKey<Long> userIdAttr = AttributeKey.valueOf(ChannelAttrKey.USER_ID);
        // Channel内部指保留一个AttributeMap<AttributeKey,Attribute<T>>
        Long userId = ctx.channel().attr(userIdAttr).get();
        log.info("handlerRemoved userID:{}",userId);
        AttributeKey<Integer> terminalAttr = AttributeKey.valueOf(ChannelAttrKey.TERMINAL_TYPE);
        Integer terminal = ctx.channel().attr(terminalAttr).get();

        ChannelHandlerContext context = UserChannelCtxMap.getChannelCtx(userId, terminal);
        // 判断一下，避免异地登录导致的误删
        if (context != null && ctx.channel().id().equals(context.channel().id())) {
            // 移除channel
            UserChannelCtxMap.removeChannelCtx(userId, terminal);
            // 用户下线
            RedisTemplate<String, Object> redisTemplate = SpringContextHolder.getBean("redisTemplate");
            String key = String.join(":", IMRedisKey.IM_USER_SERVER_ID, userId.toString(), terminal.toString());
            redisTemplate.delete(key);
            log.info("断开连接,userId:{},终端类型:{}", userId, terminal);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                // 在规定时间内没有收到客户端的上行数据, 主动断开连接
                AttributeKey<Long> attr = AttributeKey.valueOf("USER_ID");
                Long userId = ctx.channel().attr(attr).get();
                AttributeKey<Integer> terminalAttr = AttributeKey.valueOf(ChannelAttrKey.TERMINAL_TYPE);
                Integer terminal = ctx.channel().attr(terminalAttr).get();
                log.info("心跳超时，即将断开连接,用户id:{},终端类型:{} ", userId, terminal);
                ctx.channel().close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }

    }
}
