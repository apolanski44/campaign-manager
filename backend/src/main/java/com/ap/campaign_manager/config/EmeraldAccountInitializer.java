package com.ap.campaign_manager.config;

import com.ap.campaign_manager.account.AccountConstants;
import com.ap.campaign_manager.account.EmeraldAccount;
import com.ap.campaign_manager.account.EmeraldAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class EmeraldAccountInitializer implements CommandLineRunner {
    private final EmeraldAccountRepository emeraldAccountRepository;

    @Override
    public void run(String... args) {
        if (emeraldAccountRepository.existsById(AccountConstants.EMERALD_ACCOUNT_ID)) {
            return;
        }

        EmeraldAccount account = new EmeraldAccount();
        account.setId(AccountConstants.EMERALD_ACCOUNT_ID);
        account.setBalance(new BigDecimal("10000.00"));

        emeraldAccountRepository.save(account);
    }
}
