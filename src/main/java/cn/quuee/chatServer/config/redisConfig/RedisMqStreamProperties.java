package cn.quuee.chatServer.config.redisConfig;

import lombok.Data;

import java.util.List;

@Data
public class RedisMqStreamProperties {

    public String name;
    public List<RedisMqGroupProperties> groups;

}
