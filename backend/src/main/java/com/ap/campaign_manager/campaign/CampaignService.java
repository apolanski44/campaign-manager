package com.ap.campaign_manager.campaign;

import com.ap.campaign_manager.account.EmeraldAccountService;
import com.ap.campaign_manager.campaign.dto.CampaignCreationDto;
import com.ap.campaign_manager.campaign.dto.CampaignUpdateDto;
import com.ap.campaign_manager.exception.CampaignNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CampaignService {
    private final CampaignRepository campaignRepository;
    private final CampaignMapper campaignMapper;
    private final EmeraldAccountService emeraldAccountService;

    public List<Campaign> listCampaigns() {
        return campaignRepository.findAll();
    }

    @Transactional
    public Campaign createCampaign(CampaignCreationDto dto) {
        emeraldAccountService.reserveFunds(dto.getCampaignFund());

        Campaign campaign = campaignMapper.toEntity(dto);

        return campaignRepository.save(campaign);
    }

    @Transactional
    public void deleteCampaign(UUID id) {
        Campaign campaignToDelete = campaignRepository.findById(id)
                .orElseThrow(() -> new CampaignNotFoundException("Campaign not found"));

        emeraldAccountService.releaseFunds(campaignToDelete.getCampaignFund());

        campaignRepository.delete(campaignToDelete);
    }

    @Transactional
    public Campaign updateCampaign(CampaignUpdateDto dto, UUID id) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new CampaignNotFoundException("Campaign not found"));

        emeraldAccountService.adjustFunds(campaign.getCampaignFund(), dto.getCampaignFund());

        campaign.setCampaignName(dto.getCampaignName());
        campaign.setKeywords(new HashSet<>(dto.getKeywords()));
        campaign.setBidAmount(dto.getBidAmount());
        campaign.setCampaignFund(dto.getCampaignFund());
        campaign.setStatus(dto.getStatus());
        campaign.setTown(dto.getTown());
        campaign.setRadius(dto.getRadius());

        return campaignRepository.save(campaign);
    }

    public Campaign getCampaignById(UUID id) {
        return campaignRepository.findById(id)
                .orElseThrow(() -> new CampaignNotFoundException("Campaign not found"));
    }
}
