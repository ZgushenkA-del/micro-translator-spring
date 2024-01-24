package ru.ZgushenkA.textRecognition;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.ZgushenkA.textRecognition.configurations.RabbitProperties;

@SpringBootApplication
@EnableConfigurationProperties(RabbitProperties.class)
public class TextRecognitionApplication {
    public static void main(String[] args) {
        SpringApplication.run(TextRecognitionApplication.class, args);
    }

}
