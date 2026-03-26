package com.ap.campaign_manager.campaign;

import com.ap.campaign_manager.campaign.dto.CampaignCreationDto;
import com.ap.campaign_manager.campaign.dto.CampaignResponseDto;
import com.ap.campaign_manager.campaign.dto.CampaignUpdateDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/campaigns")
public class CampaignController {
    private final CampaignService campaignService;

    @GetMapping
    public List<CampaignResponseDto> getAll() {
        return campaignService.listCampaigns();
    }

    @GetMapping("/{id}")
    public CampaignResponseDto getById(@PathVariable UUID id) {
        return campaignService.getCampaignById(id);
    }

    @PutMapping("/{id}")
    public CampaignResponseDto update(@PathVariable UUID id, @RequestBody @Valid CampaignUpdateDto dto) {
        return campaignService.updateCampaign(dto, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        campaignService.deleteCampaign(id);
    }

    @PostMapping
    public CampaignResponseDto create(@RequestBody @Valid CampaignCreationDto dto) {
        return campaignService.createCampaign(dto);
    }
}
