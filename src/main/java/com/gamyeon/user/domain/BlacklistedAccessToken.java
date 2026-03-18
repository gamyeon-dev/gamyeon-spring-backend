package com.gamyeon.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "blacklisted_access_tokens")
public class BlacklistedAccessToken {

  @Id
  @Column(nullable = false, length = 64)
  private String tokenId;

  @Column(nullable = false)
  private Long userId;

  @Column(nullable = false)
  private Instant expiresAt;

  protected BlacklistedAccessToken() {}

  private BlacklistedAccessToken(String tokenId, Long userId, Instant expiresAt) {
    this.tokenId = tokenId;
    this.userId = userId;
    this.expiresAt = expiresAt;
  }

  public static BlacklistedAccessToken create(String tokenId, Long userId, Instant expiresAt) {
    return new BlacklistedAccessToken(tokenId, userId, expiresAt);
  }

  public String getTokenId() {
    return tokenId;
  }

  public Long getUserId() {
    return userId;
  }

  public Instant getExpiresAt() {
    return expiresAt;
  }
}
