package com.umutgldn.tickethub.auth.service;

import com.umutgldn.tickethub.auth.config.LoginSecurityProperties;
import com.umutgldn.tickethub.auth.exception.AccountLockedException;
import com.umutgldn.tickethub.auth.exception.InvalidCredentialsException;
import com.umutgldn.tickethub.auth.repository.UserRepository;
import com.umutgldn.tickethub.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LoginSecurityProperties loginSecurityProperties;

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void validateAccountStatus(User user) {
        if (!user.isActive()) {
            throw new InvalidCredentialsException("Invalid email or password");
        }
        if (user.isPermanentlyLocked(loginSecurityProperties.maxLockCount())) {
            throw new AccountLockedException("Account has been deactivated due to too many failed attempts. Contact your administrator");
        }

        Instant now = Instant.now();
        if (user.isLocked(now)) {
            throw new AccountLockedException("Account is temporarily locked. Try again later");
        }
    }

    @Transactional
    public void validatePassword(User user, String rawPassword) {
        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            Instant now = Instant.now();
            int lockCount = user.getLockCount();
            int maxLockCount = loginSecurityProperties.maxLockCount();

            Duration lockDuration = lockCount < maxLockCount ?
                    loginSecurityProperties.getLockDuration(lockCount)
                    : Duration.ZERO;

            user.recordFailedLogin(now,
                    loginSecurityProperties.maxFailedAttempts(),
                    maxLockCount,
                    lockDuration
            );
            userRepository.save(user);

            if (user.isPermanentlyLocked(loginSecurityProperties.maxLockCount())) {
                throw new AccountLockedException("Account has been deactivated due to too many failed attempts. Contact your administrator");
            }
            throw new InvalidCredentialsException("Invalid email or password");
        }
    }


    @Transactional
    public void onLoginSuccess(User user) {
        user.onLoginSuccess(Instant.now());
        userRepository.save(user);
    }
}
