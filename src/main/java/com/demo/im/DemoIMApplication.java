package com.demo.im;

import com.demo.im.config.minioConfig.MinioProperties;
import com.demo.im.config.redisConfig.RedisMqProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({RedisMqProperties.class, MinioProperties.class})
public class DemoIMApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoIMApplication.class,args);
    }
}
