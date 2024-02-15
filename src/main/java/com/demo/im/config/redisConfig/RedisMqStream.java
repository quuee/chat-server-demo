package com.demo.im.config.redisConfig;

import lombok.Data;

import java.util.List;

@Data
public class RedisMqStream {

    public String name;
    public List<RedisMqGroup> groups;

}
