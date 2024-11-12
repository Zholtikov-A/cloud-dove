package com.zholtikov.cloud_dove.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EMailDto {
    String to;
    String from;
    String subject;
    String content;
    private Map< String, Object > model;

}
