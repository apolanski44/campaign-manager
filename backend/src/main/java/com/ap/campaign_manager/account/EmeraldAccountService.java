package com.ap.campaign_manager.account;

import com.ap.campaign_manager.account.dto.AccountBalanceResponseDto;
import com.ap.campaign_manager.exception.EmeraldAccountNotFoundException;
import com.ap.campaign_manager.exception.InsufficientFundsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class EmeraldAccountService {
    private final EmeraldAccountRepository emeraldAccountRepository;

    public AccountBalanceResponseDto getAccountBalance() {
        BigDecimal balance =  emeraldAccountRepository.getBalanceById(AccountConstants.EMERALD_ACCOUNT_ID)
                .orElseThrow(() -> new EmeraldAccountNotFoundException("Account not found"));

        return new AccountBalanceResponseDto(balance);
    }

    public void reserveFunds(BigDecimal amount) {
        EmeraldAccount emeraldAccount = getAccount();

        if (!hasSufficientFunds(emeraldAccount.getBalance(), amount)) {
            throw new InsufficientFundsException("Not enough founds in account");
        }

        emeraldAccount.setBalance(emeraldAccount.getBalance().subtract(amount));
        emeraldAccountRepository.save(emeraldAccount);
    }

    public void releaseFunds(BigDecimal amount) {
        EmeraldAccount emeraldAccount = getAccount();

        emeraldAccount.setBalance(emeraldAccount.getBalance().add(amount));
        emeraldAccountRepository.save(emeraldAccount);
    }

    public void adjustFunds(BigDecimal newAmount, BigDecimal oldAmount) {
        BigDecimal diff = newAmount.subtract(oldAmount);

        if (diff.signum() == 0) {
            return;
        }

        if (diff.signum() > 0) {
            reserveFunds(diff);

            return;
        }

        releaseFunds(diff.abs());
    }

    private EmeraldAccount getAccount() {
        return emeraldAccountRepository.findById(AccountConstants.EMERALD_ACCOUNT_ID)
                .orElseThrow(() -> new EmeraldAccountNotFoundException("Account not found"));
    }

    private boolean hasSufficientFunds(BigDecimal balance, BigDecimal amount) {
        return balance.compareTo(amount) >= 0;
    }
}
