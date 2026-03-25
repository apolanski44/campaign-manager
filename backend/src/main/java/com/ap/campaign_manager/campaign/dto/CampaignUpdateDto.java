package com.ap.campaign_manager.campaign.dto;

import com.ap.campaign_manager.campaign.CampaignStatus;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class CampaignUpdateDto {
    @NotBlank(message = "Campaign name is required")
    private String campaignName;

    @NotEmpty(message = "Keywords are required")
    private Set<String> keywords;

    @NotNull(message = "Bid amount is required")
    @DecimalMin(value = "0.01", message = "Bid amount must be greater than 0")
    private BigDecimal bidAmount;

    @NotNull(message = "Campaign found is required")
    @DecimalMin(value = "0.01", message = "Campaign fund must be greater than 0")
    private BigDecimal campaignFund;

    @NotNull(message = "Campaign status is required")
    private CampaignStatus status;

    @NotBlank(message = "Town is required")
    private String town;

    @NotNull(message = "Radius is required")
    @Min(value = 1, message = "Radius must be at least 1 km")
    private Integer radius;
}
