package com.ap.campaign_manager.campaign;

import com.ap.campaign_manager.account.EmeraldAccountService;
import com.ap.campaign_manager.campaign.dto.CampaignCreationDto;
import com.ap.campaign_manager.campaign.dto.CampaignResponseDto;
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

    public List<CampaignResponseDto> listCampaigns() {
        List<Campaign> campaigns = campaignRepository.findAll();

        return campaigns.stream()
                .map(campaignMapper::toResponseDto)
                .toList();
    }

    @Transactional
    public CampaignResponseDto createCampaign(CampaignCreationDto dto) {
        emeraldAccountService.reserveFunds(dto.campaignFund());

        Campaign campaign = campaignMapper.toEntity(dto);

        Campaign savedCampaign = campaignRepository.save(campaign);

        return campaignMapper.toResponseDto(savedCampaign);
    }

    @Transactional
    public void deleteCampaign(UUID id) {
        Campaign campaignToDelete = campaignRepository.findById(id)
                .orElseThrow(() -> new CampaignNotFoundException("Campaign not found"));

        emeraldAccountService.releaseFunds(campaignToDelete.getCampaignFund());

        campaignRepository.delete(campaignToDelete);
    }

    @Transactional
    public CampaignResponseDto updateCampaign(CampaignUpdateDto dto, UUID id) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new CampaignNotFoundException("Campaign not found"));

        emeraldAccountService.adjustFunds(campaign.getCampaignFund(), dto.campaignFund());

        campaign.setCampaignName(dto.campaignName());
        campaign.setKeywords(new HashSet<>(dto.keywords()));
        campaign.setBidAmount(dto.bidAmount());
        campaign.setCampaignFund(dto.campaignFund());
        campaign.setStatus(dto.status());
        campaign.setTown(dto.town());
        campaign.setRadius(dto.radius());

        Campaign savedCampaign = campaignRepository.save(campaign);

        return campaignMapper.toResponseDto(savedCampaign);
    }

    public CampaignResponseDto getCampaignById(UUID id) {
        Campaign campaign =  campaignRepository.findById(id)
                .orElseThrow(() -> new CampaignNotFoundException("Campaign not found"));

        return campaignMapper.toResponseDto(campaign);
    }
}
