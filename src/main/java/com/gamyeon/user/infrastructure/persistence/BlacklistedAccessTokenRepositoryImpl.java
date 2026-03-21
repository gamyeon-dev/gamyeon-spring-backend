package com.gamyeon.user.infrastructure.persistence;

import com.gamyeon.user.application.port.outbound.BlacklistedAccessTokenRepository;
import com.gamyeon.user.domain.BlacklistedAccessToken;
import java.time.Instant;
import org.springframework.stereotype.Repository;

@Repository
public class BlacklistedAccessTokenRepositoryImpl implements BlacklistedAccessTokenRepository {

  private final BlacklistedAccessTokenJpaRepository jpaRepository;

  public BlacklistedAccessTokenRepositoryImpl(BlacklistedAccessTokenJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public void save(BlacklistedAccessToken blacklistedAccessToken) {
    jpaRepository.save(blacklistedAccessToken);
  }

  @Override
  public boolean existsByTokenId(String tokenId) {
    return jpaRepository.existsById(tokenId);
  }

  @Override
  public void deleteExpiredBefore(Instant now) {
    jpaRepository.deleteByExpiresAtBefore(now);
  }
}
