package com.gamyeon.user.infrastructure.persistence;

import com.gamyeon.user.domain.OAuthProvider;
import com.gamyeon.user.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

interface UserJpaRepository extends JpaRepository<User, Long> {

  Optional<User> findByProviderAndProviderId(OAuthProvider provider, String providerId);

  Optional<User> findByEmail(String email);
}
