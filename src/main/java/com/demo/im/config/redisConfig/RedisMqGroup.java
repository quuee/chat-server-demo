package com.demo.im.config.redisConfig;

import lombok.Data;

@Data
public class RedisMqGroup {

    private String name;

    private String[] consumers;

}
