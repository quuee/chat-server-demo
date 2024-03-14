package cn.quuee.chatServer.config.minioConfig;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 如果不加EnableConfigurationProperties，
 * 则ConfigurationProperties+Component才注入到ioc容器
 */

@ConfigurationProperties(prefix = "minio")
@Data
public class MinioProperties {

    private String endpoint;

    private String account;

    private String password;

}
