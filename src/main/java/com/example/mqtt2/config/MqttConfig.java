package com.example.mqtt2.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * MQTT属性配置类
 *
 * @author LuoXianchao
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.mqtt")
public class MqttConfig {
    private String url;
    private String clientid;
    private String username;
    private String password;
    private boolean cleansession;
    private int timeout;
    private int keepalive;
    private int connectionTimeout;

}
