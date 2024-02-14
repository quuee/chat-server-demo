package com.demo.im.netty;

import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserChannelCtxMap {

    /**
     *  维护userId和ctx的关联关系，格式:Map<userId,map<terminal,ctx>>
     *  netty服务的客户端 channel 都是存在本地的 自定义 map 或者 netty 提供的 ChannelGroup 里面
     */
    private static final Map<Long, Map<Integer, ChannelHandlerContext>> channelMap = new ConcurrentHashMap<>();

    public static void addChannelCtx(Long userId, Integer channel, ChannelHandlerContext ctx) {
        channelMap.computeIfAbsent(userId, key -> new ConcurrentHashMap<>()).put(channel, ctx);
    }

    public static void removeChannelCtx(Long userId, Integer terminal) {
        if (userId != null && terminal != null && channelMap.containsKey(userId)) {
            Map<Integer, ChannelHandlerContext> userChannelMap = channelMap.get(userId);
            userChannelMap.remove(terminal);
        }
    }

    public static ChannelHandlerContext getChannelCtx(Long userId, Integer terminal) {
        if (userId != null && terminal != null && channelMap.containsKey(userId)) {
            Map<Integer, ChannelHandlerContext> userChannelMap = channelMap.get(userId);
            if (userChannelMap.containsKey(terminal)) {
                return userChannelMap.get(terminal);
            }
        }
        return null;
    }

    public static Map<Integer, ChannelHandlerContext> getChannelCtx(Long userId) {
        if (userId == null) {
            return null;
        }
        return channelMap.get(userId);
    }

}
