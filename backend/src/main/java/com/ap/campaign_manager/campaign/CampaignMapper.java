package com.ap.campaign_manager.campaign;

import com.ap.campaign_manager.campaign.dto.CampaignCreationDto;
import com.ap.campaign_manager.campaign.dto.CampaignResponseDto;
import org.springframework.stereotype.Component;

@Component
public class CampaignMapper {
    public Campaign toEntity(CampaignCreationDto dto) {
        return Campaign.builder()
                .campaignName(dto.campaignName())
                .keywords(dto.keywords())
                .bidAmount(dto.bidAmount())
                .campaignFund(dto.campaignFund())
                .status(dto.status())
                .town(dto.town())
                .radius(dto.radius())
                .build();
    }

    public CampaignResponseDto toResponseDto(Campaign campaign) {
        return new CampaignResponseDto(
                campaign.getId(),
                campaign.getCampaignName(),
                campaign.getKeywords(),
                campaign.getBidAmount(),
                campaign.getCampaignFund(),
                campaign.getStatus(),
                campaign.getTown(),
                campaign.getRadius()
        );
    }
}
