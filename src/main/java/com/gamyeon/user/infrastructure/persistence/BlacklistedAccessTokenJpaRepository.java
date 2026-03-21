package com.gamyeon.user.infrastructure.persistence;

import com.gamyeon.user.domain.BlacklistedAccessToken;
import java.time.Instant;
import org.springframework.data.jpa.repository.JpaRepository;

interface BlacklistedAccessTokenJpaRepository
    extends JpaRepository<BlacklistedAccessToken, String> {

  void deleteByExpiresAtBefore(Instant now);
}
