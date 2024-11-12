package com.zholtikov.cloud_dove.kafka.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zholtikov.cloud_dove.dto.EMailDto;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class CustomSerializer implements Serializer<EMailDto> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, EMailDto data) {
        try {
            if (data == null) {
                System.out.println("Null received at serializing");
                return new byte[0];
            }
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new SerializationException("Error when serializing EMailDto to byte[]");
        }
    }

    @Override
    public void close() {
    }
}