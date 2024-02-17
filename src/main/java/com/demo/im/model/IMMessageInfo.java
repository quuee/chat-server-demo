package com.demo.im.model;

import lombok.Data;

import java.util.List;

@Data
public class IMMessageInfo {

    /**
     * 会话类型
     */
    private Integer conversationType;

    /**
     * 发送方
     */
    private IMUserInfo sender;

    /**
     * 接收方用户列表
     */
    List<IMUserInfo> receivers;

    /**
     * 是否需要回调发送结果
     */
//    private Boolean sendResult;

    /**
     * 当前服务名（回调发送结果使用）
     */
//    private String serviceName;

    private String contentTime;
    /**
     * 推送消息体
     */
    private Object content;

    /**
     * 消息类型 文本 图片 声音
     */
    private Integer contentType;

    private ImageInfo image;

    private SoundInfo sound;
}


