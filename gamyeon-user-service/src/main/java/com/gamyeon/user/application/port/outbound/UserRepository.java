package com.gamyeon.user.application.port.outbound;

import com.gamyeon.user.domain.user.OAuthProvider;
import com.gamyeon.user.domain.user.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findByProviderAndProviderId(OAuthProvider provider, String providerId);

    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);

    User save(User user);
}
