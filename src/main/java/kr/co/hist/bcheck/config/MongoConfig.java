package kr.co.hist.bcheck.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix="spring.mongo")
public class MongoConfig {
    private int port;

    private String host;

    private String user;

    private String database;

    private String password;
}
