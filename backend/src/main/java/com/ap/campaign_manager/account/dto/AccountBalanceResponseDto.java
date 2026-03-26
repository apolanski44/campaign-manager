package com.ap.campaign_manager.account.dto;

import java.math.BigDecimal;


public record AccountBalanceResponseDto (
        BigDecimal balance
) {
}
