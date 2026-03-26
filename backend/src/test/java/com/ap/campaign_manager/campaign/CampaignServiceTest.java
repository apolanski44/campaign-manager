package com.ap.campaign_manager.campaign;

import com.ap.campaign_manager.account.EmeraldAccountService;
import com.ap.campaign_manager.campaign.dto.CampaignCreationDto;
import com.ap.campaign_manager.campaign.dto.CampaignResponseDto;
import com.ap.campaign_manager.campaign.dto.CampaignUpdateDto;
import com.ap.campaign_manager.exception.CampaignNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CampaignServiceTest {
    @Mock
    private CampaignRepository campaignRepository;
    
    @Mock
    private CampaignMapper campaignMapper;
    
    @Mock
    private EmeraldAccountService emeraldAccountService;

    @InjectMocks
    private CampaignService campaignService;

    @Test
    void listCampaignsReturnsEmptyListWhenNoCampaignsFound() {
        when(campaignRepository.findAll())
                .thenReturn(List.of());

        List<CampaignResponseDto> result = campaignService.listCampaigns();

        assertThat(result).isEmpty();
        assertThat(result).isNotNull();
    }

    @Test
    void listCampaignsReturnsMappedDtoList() {
        Campaign campaign1 = new Campaign();
        Campaign campaign2 = new Campaign();
        List<Campaign> campaignsFromDb = List.of(campaign1, campaign2);
        CampaignResponseDto responseDto =  new CampaignResponseDto(
                UUID.fromString("4d63d74b-c34b-4f50-8ee4-62c180e0ca35"),
                "Summer Sale",
                Set.of("sale", "summer"),
                new BigDecimal("2.50"),
                new BigDecimal("1000"),
                CampaignStatus.ON,
                "Warszawa",
                10
        );

        when(campaignRepository.findAll())
                .thenReturn(campaignsFromDb);

        when(campaignMapper.toResponseDto(any()))
                .thenReturn(responseDto);

        List<CampaignResponseDto> result = campaignService.listCampaigns();

        assertThat(result).hasSize(2);

        verify(campaignRepository).findAll();
        verify(campaignMapper, times(2)).toResponseDto(any());
    }

    @Test
    void createCampaignReturnsCampaignResponseDto() {
        CampaignCreationDto dto =  new CampaignCreationDto(
                "Summer Sale",
                Set.of("sale", "summer"),
                new BigDecimal("2.50"),
                new BigDecimal("1000"),
                CampaignStatus.ON,
                "Warszawa",
                10
        );

        CampaignResponseDto responseDto =  new CampaignResponseDto(
                UUID.fromString("4d63d74b-c34b-4f50-8ee4-62c180e0ca35"),
                "Summer Sale",
                Set.of("sale", "summer"),
                new BigDecimal("2.50"),
                new BigDecimal("1000"),
                CampaignStatus.ON,
                "Warszawa",
                10
        );

        Campaign mappedEntity = new Campaign();
        mappedEntity.setCampaignName("Summer Sale");

        when(campaignMapper.toEntity(dto))
                .thenReturn(mappedEntity);

        when(campaignRepository.save(any()))
                .thenReturn(mappedEntity);

        when(campaignMapper.toResponseDto(any()))
                .thenReturn(responseDto);

        campaignService.createCampaign(dto);

        verify(emeraldAccountService).reserveFunds(dto.campaignFund());

        ArgumentCaptor<Campaign> campaignCaptor = ArgumentCaptor.forClass(Campaign.class);

        verify(campaignRepository).save(campaignCaptor.capture());

        Campaign savedCampaign = campaignCaptor.getValue();

        assertThat(savedCampaign.getCampaignName()).isEqualTo(dto.campaignName());
    }

    @Test
    void deleteCampaignReleasesFundsAndDeleteFromRepo() {
        UUID campaignId =  UUID.fromString("4d63d74b-c34b-4f50-8ee4-62c180e0ca35");
        BigDecimal fundToRelease = new BigDecimal("500.00");

        Campaign campaign = new Campaign();
        campaign.setId(campaignId);
        campaign.setCampaignFund(fundToRelease);

        when(campaignRepository.findById(campaignId))
                .thenReturn(Optional.of(campaign));

        campaignService.deleteCampaign(campaignId);

        verify(emeraldAccountService).releaseFunds(fundToRelease);
        verify(campaignRepository).delete(campaign);
    }

    @Test
    void deleteCampaignThrowsExceptionWhenCampaignNotFound() {
        UUID campaignId = UUID.randomUUID();
        when(campaignRepository.findById(campaignId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(CampaignNotFoundException.class)
                .isThrownBy(() -> campaignService.deleteCampaign(campaignId))
                .withMessage("Campaign not found");

        verifyNoInteractions(emeraldAccountService);
        verify(campaignRepository, never()).delete(any());
    }

    @Test
    void updateCampaignAdjustsFundsAndSavesUpdatedEntity() {
        UUID campaignId =  UUID.fromString("4d63d74b-c34b-4f50-8ee4-62c180e0ca35");
        BigDecimal oldFund = new BigDecimal("1000");
        BigDecimal newFund = new BigDecimal("1500");

        CampaignUpdateDto dto = new CampaignUpdateDto(
                "Updated Name",
                Set.of("new"),
                new BigDecimal("5"),
                newFund,
                CampaignStatus.ON,
                "Kraków",
                20
        );

        Campaign existingCampaign = new Campaign();
        existingCampaign.setId(campaignId);
        existingCampaign.setCampaignFund(oldFund);
        existingCampaign.setCampaignName("Old Name");

        when(campaignRepository.findById(campaignId))
                .thenReturn(Optional.of(existingCampaign));

        when(campaignRepository.save(any()))
                .thenReturn(existingCampaign);

        when(campaignMapper.toResponseDto(any()))
                .thenReturn(new CampaignResponseDto(
                        campaignId,
                        "Updated Name",
                        Set.of("new"), new BigDecimal("5.00"),
                        newFund, CampaignStatus.ON,
                        "Kraków",
                        20
                ));

        campaignService.updateCampaign(dto, campaignId);

        ArgumentCaptor<Campaign> captor = ArgumentCaptor.forClass(Campaign.class);

        verify(campaignRepository).save(captor.capture());
        verify(emeraldAccountService).adjustFunds(newFund, oldFund);

        assertThat(captor.getValue().getCampaignName()).isEqualTo("Updated Name");
        assertThat(captor.getValue().getCampaignFund()).isEqualByComparingTo(newFund);
    }

    @Test
    void updateCampaignThrowsExceptionWhenCampaignNotFound() {
        UUID campaignId =  UUID.fromString("4d63d74b-c34b-4f50-8ee4-62c180e0ca35");
        BigDecimal newFund = new BigDecimal("1500");
        CampaignUpdateDto dto = new CampaignUpdateDto(
                "Updated Name",
                Set.of("new"),
                new BigDecimal("5"),
                newFund,
                CampaignStatus.ON,
                "Kraków",
                20
        );

        when(campaignRepository.findById(campaignId))
                .thenReturn(Optional.empty());

        assertThatExceptionOfType(CampaignNotFoundException.class)
                .isThrownBy(() -> campaignService.updateCampaign(dto, campaignId))
                .withMessage("Campaign not found");

        verify(campaignRepository).findById(campaignId);
        verifyNoInteractions(emeraldAccountService);
        verify(campaignRepository, never()).save(any());
        verifyNoInteractions(campaignMapper);
    }

    @Test
    void getCampaignByIdReturnsCampaign() {
        UUID campaignId =  UUID.fromString("4d63d74b-c34b-4f50-8ee4-62c180e0ca35");
        Campaign campaign = new Campaign();
        campaign.setId(campaignId);
        campaign.setCampaignName("Summer Sale");

        CampaignResponseDto expectedResponse =  new CampaignResponseDto(
                campaignId,
                "Summer Sale",
                Set.of("sale", "summer"),
                new BigDecimal("2.50"),
                new BigDecimal("1000"),
                CampaignStatus.ON,
                "Warszawa",
                10
        );

        when(campaignRepository.findById(campaignId))
                .thenReturn(Optional.of(campaign));

        when(campaignMapper.toResponseDto(campaign))
                .thenReturn(expectedResponse);

        CampaignResponseDto result = campaignService.getCampaignById(campaignId);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(campaignId);
        assertThat(result.campaignName()).isEqualTo("Summer Sale");
        verify(campaignRepository).findById(campaignId);
    }

    @Test
    void getCampaignByIdThrowsExceptionWhenCampaignNotFound() {
        UUID campaignId =  UUID.fromString("4d63d74b-c34b-4f50-8ee4-62c180e0ca35");

        when(campaignRepository.findById(campaignId))
                .thenReturn(Optional.empty());

        assertThatExceptionOfType(CampaignNotFoundException.class)
                .isThrownBy(() -> campaignService.getCampaignById(campaignId))
                .withMessage("Campaign not found");

        verifyNoInteractions(campaignMapper);
    }
}
