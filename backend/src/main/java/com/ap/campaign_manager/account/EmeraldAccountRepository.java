package com.ap.campaign_manager.account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmeraldAccountRepository extends JpaRepository<EmeraldAccount, UUID> {
    Optional<EmeraldAccount> findById(UUID id);
}
