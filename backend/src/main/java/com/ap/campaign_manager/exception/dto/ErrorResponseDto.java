package com.ap.campaign_manager.exception.dto;

import java.util.Map;

public record ErrorResponseDto(
        int status,
        String error,
        String message,
        Map<String, String> validationErrors
) {
}
