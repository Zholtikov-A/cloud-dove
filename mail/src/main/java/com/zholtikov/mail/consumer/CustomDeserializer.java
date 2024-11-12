package com.zholtikov.mail.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zholtikov.mail.dto.EMailDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

@Slf4j
public class CustomDeserializer implements Deserializer<EMailDto> {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public EMailDto deserialize(String topic, byte[] data) {
        try {
            if (data == null){
                return new EMailDto();
            }
            return objectMapper.readValue(new String(data, "UTF-8"), EMailDto.class);
        } catch (Exception e) {
            throw new SerializationException("Error when deserializing byte[] to EMailDto");
        }
    }

    @Override
    public void close() {
    }
}