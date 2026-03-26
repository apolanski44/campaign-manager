package com.ap.campaign_manager.account;

import com.ap.campaign_manager.account.dto.AccountBalanceResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/account")
public class EmeraldAccountController {
    private final EmeraldAccountService emeraldAccountService;

    @GetMapping("/balance")
    public AccountBalanceResponseDto getBalance() {
        return emeraldAccountService.getAccountBalance();
    }
}
