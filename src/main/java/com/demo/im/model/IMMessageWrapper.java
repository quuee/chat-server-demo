package com.demo.im.model;

import lombok.Data;

@Data
public class IMMessageWrapper<T> {
    /**
     * 命令
     */
    private Integer cmd;

    /**
     * 推送消息体
     */
    private T data;
}
