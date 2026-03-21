package com.gamyeon.user.application.port.outbound;

import com.gamyeon.user.domain.BlacklistedAccessToken;
import java.time.Instant;

public interface BlacklistedAccessTokenRepository {

  void save(BlacklistedAccessToken blacklistedAccessToken);

  boolean existsByTokenId(String tokenId);

  void deleteExpiredBefore(Instant now);
}
