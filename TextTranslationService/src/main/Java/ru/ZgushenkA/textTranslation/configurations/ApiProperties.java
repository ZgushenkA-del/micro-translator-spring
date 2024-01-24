package ru.ZgushenkA.textTranslation.configurations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "properties.api")
@Getter
@Setter
public class ApiProperties {
    String apiKey;
}
