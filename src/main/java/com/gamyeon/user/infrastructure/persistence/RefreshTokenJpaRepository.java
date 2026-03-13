package com.gamyeon.user.infrastructure.persistence;

import com.gamyeon.user.domain.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

interface RefreshTokenJpaRepository extends JpaRepository<RefreshToken, Long> {

  Optional<RefreshToken> findByToken(String token);

  void deleteByUserId(Long userId);
}
