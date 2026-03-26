package com.ap.campaign_manager.campaign.dto;

import com.ap.campaign_manager.campaign.CampaignStatus;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

public record CampaignResponseDto(
        UUID id,
        String campaignName,
        Set<String> keywords,
        BigDecimal bidAmount,
        BigDecimal campaignFund,
        CampaignStatus status,
        String town,
        Integer radius
) {
}
