package com.ap.campaign_manager.campaign;

import com.ap.campaign_manager.campaign.dto.CampaignCreationDto;
import org.springframework.stereotype.Component;

@Component
public class CampaignMapper {
    public Campaign toEntity(CampaignCreationDto dto) {
        return Campaign.builder()
                .campaignName(dto.getCampaignName())
                .keywords(dto.getKeywords())
                .bidAmount(dto.getBidAmount())
                .campaignFund(dto.getCampaignFund())
                .status(dto.getStatus())
                .town(dto.getTown())
                .radius(dto.getRadius())
                .build();
    }
}
