package com.ap.campaign_manager.account;

import com.ap.campaign_manager.account.dto.AccountBalanceResponseDto;
import com.ap.campaign_manager.exception.EmeraldAccountNotFoundException;
import com.ap.campaign_manager.exception.InsufficientFundsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmeraldAccountServiceTest {
    @Mock
    private EmeraldAccountRepository emeraldAccountRepository;

    @InjectMocks
    private EmeraldAccountService emeraldAccountService;

    @Test
    void getAccountBalanceThrowsExceptionWhenAccountNotFound() {
        UUID id = AccountConstants.EMERALD_ACCOUNT_ID;

        assertThatExceptionOfType(EmeraldAccountNotFoundException.class)
                .isThrownBy(() -> emeraldAccountService.getAccountBalance())
                .withMessage("Account not found");

        verify(emeraldAccountRepository).getBalanceById(id);
    }

    @Test
    void getAccountBalanceReturnsCorrectValue() {
        BigDecimal expectedBalance = new BigDecimal("1000");
        UUID id = AccountConstants.EMERALD_ACCOUNT_ID;

        when(emeraldAccountRepository.getBalanceById(AccountConstants.EMERALD_ACCOUNT_ID))
                .thenReturn(Optional.of(expectedBalance));

        AccountBalanceResponseDto response = emeraldAccountService.getAccountBalance();

        assertThat(response.balance()).isEqualByComparingTo(expectedBalance);

        verify(emeraldAccountRepository).getBalanceById(id);
    }

    @Test
    void reserveFoundsThrowsExceptionsWhenNotEnoughFoundsInAccount() {
        EmeraldAccount account = new EmeraldAccount();
        UUID id = AccountConstants.EMERALD_ACCOUNT_ID;

        account.setBalance(new BigDecimal("100"));

        when(emeraldAccountRepository.findById(id))
                .thenReturn(Optional.of(account));

        assertThatExceptionOfType(InsufficientFundsException.class)
                .isThrownBy(() -> emeraldAccountService.reserveFunds(new BigDecimal("200")))
                .withMessage("Not enough founds in account");

        verify(emeraldAccountRepository).findById(id);
    }

    @Test
    void reserveFundsUpdatesBalanceCorrectly() {
        BigDecimal initialBalance = new BigDecimal("100");
        BigDecimal amountToReserve = new BigDecimal("30");
        BigDecimal expectedBalance = new BigDecimal("70");
        UUID id = AccountConstants.EMERALD_ACCOUNT_ID;

        EmeraldAccount account = new EmeraldAccount();
        account.setBalance(initialBalance);

        when(emeraldAccountRepository.findById(id))
                .thenReturn(Optional.of(account));

        emeraldAccountService.reserveFunds(amountToReserve);

        ArgumentCaptor<EmeraldAccount> accountCaptor = ArgumentCaptor.forClass(EmeraldAccount.class);

        verify(emeraldAccountRepository).save(accountCaptor.capture());
        verify(emeraldAccountRepository).findById(id);

        EmeraldAccount savedAccount = accountCaptor.getValue();

        assertThat(savedAccount.getBalance()).isEqualByComparingTo(expectedBalance);
    }

    @Test
    void releaseFundsUpdatesBalanceCorrectly() {
        BigDecimal initialBalance = new BigDecimal("100");
        BigDecimal amountToRelease = new BigDecimal("30");
        BigDecimal expectedBalance = new BigDecimal("130");
        UUID id = AccountConstants.EMERALD_ACCOUNT_ID;

        EmeraldAccount account = new EmeraldAccount();
        account.setBalance(initialBalance);

        when(emeraldAccountRepository.findById(id))
                .thenReturn(Optional.of(account));

        emeraldAccountService.releaseFunds(amountToRelease);

        ArgumentCaptor<EmeraldAccount> accountCaptor = ArgumentCaptor.forClass(EmeraldAccount.class);

        verify(emeraldAccountRepository).save(accountCaptor.capture());
        verify(emeraldAccountRepository).findById(id);

        EmeraldAccount savedAccount = accountCaptor.getValue();

        assertThat(savedAccount.getBalance()).isEqualByComparingTo(expectedBalance);
    }

    @Test
    void adjustFoundsReturnsWhenDifferenceIsZero() {
        BigDecimal newAmount = new BigDecimal("100");
        BigDecimal oldAmount = new BigDecimal("100");
        BigDecimal expectedBalance = new BigDecimal("130");

        emeraldAccountService.adjustFunds(newAmount, oldAmount);

        verifyNoInteractions(emeraldAccountRepository);
    }

    @Test
    void adjustFoundsCallsReserveFunds() {
        BigDecimal newAmount = new BigDecimal("150");
        BigDecimal oldAmount = new BigDecimal("100");
        BigDecimal expectedBalance = new BigDecimal("950");

        EmeraldAccount account = new EmeraldAccount();
        account.setBalance(new BigDecimal("1000"));

        when(emeraldAccountRepository.findById(any()))
                .thenReturn(Optional.of(account));

        emeraldAccountService.adjustFunds(newAmount, oldAmount);

        ArgumentCaptor<EmeraldAccount> captor = ArgumentCaptor.forClass(EmeraldAccount.class);

        verify(emeraldAccountRepository).save(captor.capture());

        EmeraldAccount savedAccount = captor.getValue();

        assertThat(savedAccount.getBalance()).isEqualByComparingTo(expectedBalance);
    }

    @Test
    void adjustFundsCallsReleaseFunds() {
        BigDecimal newAmount = new BigDecimal("100");
        BigDecimal oldAmount = new BigDecimal("150");
        BigDecimal expectedBalance = new BigDecimal("1050");

        EmeraldAccount account = new EmeraldAccount();
        account.setBalance(new BigDecimal("1000"));

        when(emeraldAccountRepository.findById(any()))
                .thenReturn(Optional.of(account));

        emeraldAccountService.adjustFunds(newAmount, oldAmount);

        ArgumentCaptor<EmeraldAccount> captor = ArgumentCaptor.forClass(EmeraldAccount.class);

        verify(emeraldAccountRepository).save(captor.capture());

        EmeraldAccount savedAccount = captor.getValue();

        assertThat(savedAccount.getBalance()).isEqualByComparingTo(expectedBalance);
    }
}
