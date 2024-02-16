package com.demo.im.common.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum IMMessageType {

    Text(101, "文本内容"),
    Picture(102, "图片"),
    Voice(103, "语音消息"),
    Video(104, "视频消息"),
    AtText(105, "@消息"),
    Location(106, "定位消息");

    private final Integer code;

    private final String desc;


    public static IMMessageType fromCode(Integer code) {
        for (IMMessageType typeEnum : values()) {
            if (typeEnum.code.equals(code)) {
                return typeEnum;
            }
        }
        return null;
    }


    public Integer code() {
        return this.code;
    }
}
