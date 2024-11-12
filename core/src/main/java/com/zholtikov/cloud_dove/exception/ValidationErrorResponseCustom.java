package com.zholtikov.cloud_dove.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
@Schema(name = "ValidationErrorResponseCustom")
public class ValidationErrorResponseCustom {

    private final List<Violation> violations;

}

