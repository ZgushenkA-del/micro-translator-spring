package ru.ZgushenkA.textTranslation.configurations;

import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class RabbitmqConfiguration {
    public final RabbitProperties rabbitProperties;
    static final String queueName = "result";

    @Bean
    String getTopicExchangeName() {
        return rabbitProperties.getTopicExchangeName();
    }

    @Bean
    Queue queue() {
        return new Queue(queueName, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(rabbitProperties.getTopicExchangeName());
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("trt.result.#");
    }

}
