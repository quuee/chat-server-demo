package cn.quuee.chatServer;

import cn.quuee.chatServer.config.minioConfig.MinioProperties;
import cn.quuee.chatServer.config.redisConfig.RedisMqProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({RedisMqProperties.class, MinioProperties.class})
public class ChatServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatServerApplication.class,args);
    }
}
