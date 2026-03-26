package com.ap.campaign_manager.campaign.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;


public record AccountBalanceResponseDto (
        BigDecimal balance
) {
}
