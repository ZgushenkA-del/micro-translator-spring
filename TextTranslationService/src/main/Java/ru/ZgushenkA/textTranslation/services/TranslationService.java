package ru.ZgushenkA.textTranslation.services;

import com.deepl.api.DeepLException;
import com.deepl.api.TextResult;
import com.deepl.api.Translator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import ru.ZgushenkA.textTranslation.configurations.ApiProperties;
import ru.ZgushenkA.textTranslation.configurations.RabbitProperties;

@Service
@AllArgsConstructor
@Slf4j
public class TranslationService {
    ApiProperties apiProperties;
    RabbitProperties rabbitProperties;
    RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "translation")
    public void receiveMessage(Message message) {
        try {
            handleMessage(message);
            var result = translate(new String(message.getBody()));
            log.info(result);
        } catch (DeepLException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void handleMessage(Message messageIn) throws DeepLException, InterruptedException {
        String uuid = messageIn.getMessageProperties().getHeader("uuid");
        String text = new String(messageIn.getBody());
        var result = translate(text);
        Message messageOut = MessageBuilder.withBody(result.getBytes())
                .setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN)
                .setHeader("uuid", uuid)
                .build();
        rabbitTemplate.send(rabbitProperties.getTopicExchangeName(), "trt.result.text", messageOut);
    }

    private String translate(String text) throws DeepLException, InterruptedException {
        if (text.length() == 0){
            return "";
        }
        Translator translator = new Translator(apiProperties.getApiKey());
        TextResult result = translator.translateText(text,null,"ru");
        return result.getText();
    }
}
