package com.demo.im.config.redisConfig;

import lombok.Data;

@Data
public class RedisMqGroupProperties {

    private String name;

    private String[] consumers;

}
