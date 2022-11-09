package dev.jacek.grzegorczyk.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class RabbitController {

    @RabbitListener(queues = "notifier")
    public void rabbitListener(String message) {
        log.info("!!! Message received: {}", message);
    }
}
