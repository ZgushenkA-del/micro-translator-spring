package ru.ZgushenkA.webAppService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.ZgushenkA.webAppService.configurations.RabbitProperties;

@SpringBootApplication
@EnableConfigurationProperties(RabbitProperties.class)
@EnableScheduling
public class WebApplication {

    public static void main(String[] args) {

        SpringApplication.run(WebApplication.class, args);
    }

}
