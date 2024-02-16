package com.demo.im.model;

import lombok.Data;

@Data
public class IMMessageWrapper<T> {
    /**
     * 会话类型
     */
    private Integer conversationType;

    /**
     * 推送消息体
     */
    private T data;
}
