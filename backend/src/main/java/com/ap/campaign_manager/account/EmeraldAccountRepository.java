package com.ap.campaign_manager.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface EmeraldAccountRepository extends JpaRepository<EmeraldAccount, UUID> {
    @Query("SELECT e.balance FROM EmeraldAccount e WHERE e.id = :id")
    Optional<BigDecimal> getBalanceById(UUID id);
}
