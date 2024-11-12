

package com.zholtikov.mail.consumer;

import com.zholtikov.mail.dto.EMailDto;
import com.zholtikov.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaConsumer {
    private final MailService mailService;

    @KafkaListener(topics = "mail-topic", containerFactory = "kafkaEventListenerContainerFactory", groupId = "my-group-id")
    public void listen(EMailDto mailDto) {
        System.out.println("Kafka listens to mail events");
        mailService.sendEmail(mailDto);
    }


}

