package com.demo.im.common.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum IMConversationType {


    HEART_BEAT(1, "心跳"),

    FORCE_LOGOUT(2, "强制下线"),

    PRIVATE_MESSAGE(3, "私聊消息"),

    GROUP_MESSAGE(4, "群发消息");

    private final Integer code;

    private final String desc;


    public static IMConversationType fromCode(Integer code) {
        for (IMConversationType typeEnum : values()) {
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
