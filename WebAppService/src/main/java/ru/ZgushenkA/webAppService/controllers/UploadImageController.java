package ru.ZgushenkA.webAppService.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import ru.ZgushenkA.webAppService.configurations.RabbitProperties;
import ru.ZgushenkA.webAppService.utils.Result;
import ru.ZgushenkA.webAppService.utils.enums.ModelLanguages;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

@Controller
@Slf4j
@SessionAttributes("modelLanguages")
@AllArgsConstructor
public class UploadImageController {
    private final RabbitTemplate rabbitTemplate;
    private final RabbitProperties rabbitProperties;
    HashMap<UUID, Result> resultMap;

    @RabbitListener(queues = "result")
    public void receiveMessage(Message message) {
        String uuid = message.getMessageProperties().getHeader("uuid");
        String result = new String(message.getBody());
        resultMap.put(UUID.fromString(uuid), new Result(result, System.currentTimeMillis()));
    }

    @PostMapping("/uploadimage")
    public String postImage(Model model, @RequestParam("image") MultipartFile file, @RequestParam(name = "modelLanguage") ModelLanguages language) throws InterruptedException, IOException {
        UUID uuid = UUID.randomUUID();
        Message message = MessageBuilder.withBody(file.getBytes()).setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN).setHeader("language", language.getRef()).setHeader("uuid", uuid).build();
        rabbitTemplate.send(rabbitProperties.getTopicExchangeName(), "trt.pic.image", message);
        model.addAttribute("language", language.getValue());
        while (!resultMap.containsKey(uuid)) {
            Thread.sleep(500);
        }
        model.addAttribute("result", resultMap.get(uuid).text());
        return "uploadimage";
    }

    @Scheduled(fixedRate = 20000)
    public void deleteOldResults() {
        var keys = resultMap.keySet();
        for (var key : keys) {
            if (resultMap.containsKey(key) && System.currentTimeMillis() - resultMap.get(key).timestamp() >= 1000 * 60 * 5) {
                resultMap.remove(key);
            }
        }
    }

    @GetMapping("/uploadimage")
    public String displayUploadForm(Model model) {
        model.addAttribute("modelLanguages", ModelLanguages.values());
        return "uploadimage";
    }
}
