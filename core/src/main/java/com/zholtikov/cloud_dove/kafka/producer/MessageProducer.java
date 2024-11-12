package com.zholtikov.cloud_dove.kafka.producer;

import com.zholtikov.cloud_dove.dto.EMailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageProducer {

    @Autowired
    private KafkaTemplate<String, EMailDto> kafkaEventTemplate;

    public void sendEventMessage(String topic, EMailDto message) {
        kafkaEventTemplate.send(topic, message);
    }

}