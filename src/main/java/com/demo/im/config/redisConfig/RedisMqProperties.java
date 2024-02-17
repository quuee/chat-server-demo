package com.demo.im.config.redisConfig;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;



@ConfigurationProperties(prefix = "redis.mq")
@Data
public class RedisMqProperties {
    public List<RedisMqStreamProperties> streams;

    public List<RedisMqStreamProperties> getStreams() {
        return streams;
    }

    public void setStreams(List<RedisMqStreamProperties> streams) {
        this.streams = streams;
    }
}

