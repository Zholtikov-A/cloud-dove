
package com.zholtikov.cloud_dove.kafka.producer;

import com.zholtikov.cloud_dove.dto.EMailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaStringTemplate;

    public void sendMessage(String topic, String message) {
        kafkaStringTemplate.send(topic, message);
    }

    @Autowired
    private KafkaTemplate<String, EMailDto> kafkaEventTemplate;

    public void sendEMail(String topic, EMailDto message) {
        kafkaEventTemplate.send(topic, message);
    }

}



