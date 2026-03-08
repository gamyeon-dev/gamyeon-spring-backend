package com.gamyeon.user.infrastructure.persistence;

import com.gamyeon.user.domain.user.OAuthProvider;
import com.gamyeon.user.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface UserJpaRepository extends JpaRepository<User, Long> {

    Optional<User> findByProviderAndProviderId(OAuthProvider provider, String providerId);

    Optional<User> findByEmail(String email);
}
