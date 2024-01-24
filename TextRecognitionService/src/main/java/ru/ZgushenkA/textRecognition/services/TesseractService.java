package ru.ZgushenkA.textRecognition.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import ru.ZgushenkA.textRecognition.configurations.RabbitProperties;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@Slf4j
@AllArgsConstructor
public class TesseractService {
    RabbitTemplate rabbitTemplate;
    RabbitProperties rabbitProperties;
    public String recognizeTextFromImage(byte[] bytes, String lang) throws TesseractException,
            IOException {
        InputStream is = new ByteArrayInputStream(bytes);
        BufferedImage image = ImageIO.read(is);
        log.info(lang);
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("TextRecognitionService/src/main/resources/tessdata");
        tesseract.setLanguage(lang);
        tesseract.setPageSegMode(1);
        /*  PSM_OSD_ONLY	    0	Orientation and script detection only.
            PSM_AUTO_OSD	    1	Automatic page segmentation with orientation and script detection. (OSD)
            PSM_AUTO_ONLY	    2	Automatic page segmentation, but no OSD, or OCR.
            PSM_AUTO	        3	Fully automatic page segmentation, but no OSD.
            PSM_SINGLE_COLUMN	4	Assume a single column of text of variable sizes.
            PSM_SINGLE_BLOCK_VERT_TEXT	5	Assume a single uniform block of vertically aligned text.
            PSM_SINGLE_BLOCK	6	Assume a single uniform block of text. (Default.)
            PSM_SINGLE_LINE	    7	Treat the image as a single text line.
            PSM_SINGLE_WORD	    8	Treat the image as a single word.
            PSM_CIRCLE_WORD	    9	Treat the image as a single word in a circle.
            PSM_SINGLE_CHAR	    10	Treat the image as a single character.
            PSM_SPARSE_TEXT	    11	Find as much text as possible in no particular order.
            PSM_SPARSE_TEXT_OSD	12	Sparse text with orientation and script det.
            PSM_RAW_LINE	    13	Treat the image as a single text line, bypassing hacks that are Tesseract-specific.*/
        tesseract.setTessVariable("user_defined_dpi", "100");
        tesseract.setOcrEngineMode(1);
        String result = tesseract.doOCR(image);
        return result;
    }

    private void handleMessage(Message messageIn) throws TesseractException, IOException {
        String lang = messageIn.getMessageProperties().getHeader("language");
        String uuid = messageIn.getMessageProperties().getHeader("uuid");
        byte[] image = messageIn.getBody();
        var result = recognizeTextFromImage(image, lang);
        Message messageOut = MessageBuilder.withBody(result.getBytes())
                .setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN)
                .setHeader("uuid", uuid)
                .build();
        rabbitTemplate.send(rabbitProperties.getTopicExchangeName(), "trt.text.translate", messageOut);
    }

    @RabbitListener(queues = "tess-recognition")
    public void receiveMessage(Message message) {
        try {
            handleMessage(message);
        } catch (TesseractException e) {
            log.info("Тессеракт Неудача");
        } catch (IOException e) {
            log.info("Ошибка в файле");
        }
    }
}
