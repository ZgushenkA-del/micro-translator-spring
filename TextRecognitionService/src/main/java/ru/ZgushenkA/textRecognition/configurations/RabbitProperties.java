package ru.ZgushenkA.textRecognition.configurations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "properties.rabbit")
@Getter
@Setter
public class RabbitProperties {
    String topicExchangeName;
}
