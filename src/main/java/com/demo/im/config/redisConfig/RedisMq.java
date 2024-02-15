package com.demo.im.config.redisConfig;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@EnableConfigurationProperties
@Configuration
@ConfigurationProperties(prefix = "redis.mq")
public class RedisMq {
    public List<RedisMqStream> streams;

    public List<RedisMqStream> getStreams() {
        return streams;
    }

    public void setStreams(List<RedisMqStream> streams) {
        this.streams = streams;
    }
}

