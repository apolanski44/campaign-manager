package com.ap.campaign_manager.campaign.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class AccountBalanceResponseDto {
    private BigDecimal balance;
}
