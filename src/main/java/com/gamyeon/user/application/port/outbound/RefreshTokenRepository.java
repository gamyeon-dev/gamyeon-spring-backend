package com.gamyeon.user.application.port.outbound;

import com.gamyeon.user.domain.RefreshToken;
import java.util.Optional;

public interface RefreshTokenRepository {

  RefreshToken save(RefreshToken refreshToken);

  Optional<RefreshToken> findByToken(String token);

  void deleteByUserId(Long userId);
}
