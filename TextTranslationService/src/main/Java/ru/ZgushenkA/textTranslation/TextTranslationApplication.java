package ru.ZgushenkA.textTranslation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.ZgushenkA.textTranslation.configurations.ApiProperties;
import ru.ZgushenkA.textTranslation.configurations.RabbitProperties;

@SpringBootApplication
@EnableConfigurationProperties({ApiProperties.class, RabbitProperties.class})
public class TextTranslationApplication {
    public static void main(String[] args) {
        SpringApplication.run(TextTranslationApplication.class, args);
    }
}
