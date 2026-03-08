package com.gamyeon.user.infrastructure.persistence;

import com.gamyeon.user.application.port.outbound.UserRepository;
import com.gamyeon.user.domain.user.OAuthProvider;
import com.gamyeon.user.domain.user.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository jpaRepository;

    public UserRepositoryImpl(UserJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<User> findByProviderAndProviderId(OAuthProvider provider, String providerId) {
        return jpaRepository.findByProviderAndProviderId(provider, providerId);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public User save(User user) {
        return jpaRepository.save(user);
    }
}
