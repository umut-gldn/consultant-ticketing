package com.umutgldn.tickethub.auth.service;

import com.umutgldn.tickethub.auth.dto.AuthResponse;
import com.umutgldn.tickethub.auth.dto.LoginRequest;
import com.umutgldn.tickethub.auth.dto.RefreshRequest;
import com.umutgldn.tickethub.auth.entity.RefreshToken;
import com.umutgldn.tickethub.auth.exception.InvalidCredentialsException;
import com.umutgldn.tickethub.auth.exception.InvalidTokenException;
import com.umutgldn.tickethub.auth.exception.TokenExpiredException;
import com.umutgldn.tickethub.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtService jwtService;
    private final TokenService tokenService;

    @Transactional
    public AuthResponse login(LoginRequest request, String deviceinfo, String ipAddress) {
        User user = userService.findByEmail(request.email())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        userService.validateAccountStatus(user);

        userService.validatePassword(user, request.password());

        userService.onLoginSuccess(user);
        log.info("User logged in :{}", user.getEmail());

        return generateToken(user, deviceinfo, ipAddress);
    }

    @Transactional
    public AuthResponse refresh(RefreshRequest request, String deviceInfo, String ipAddress) {
        RefreshToken existingToken = tokenService.findByPlainToken(request.refreshToken())
                .orElseThrow(() -> new InvalidTokenException("Refresh token not found"));

        if (existingToken.isRevoked()) {
            log.warn("Token reuse detected for user: {}", existingToken.getUser().getEmail());
            tokenService.revokeAllByUser(existingToken.getUser().getId());
            throw new InvalidTokenException("Token reuse detected. All sessions revoked");
        }
        if(existingToken.isExpired(Instant.now())){
            throw  new TokenExpiredException("Refresh token has expired");
        }
        tokenService.revoke(existingToken);
        User user=existingToken.getUser();
        log.info("Token refreshed for user :{}", user.getEmail());
        return generateToken(user, deviceInfo, ipAddress);
    }

    @Transactional
    public void logout(String refreshToken){
        RefreshToken token=tokenService.findByPlainToken(refreshToken)
                .orElseThrow(() -> new InvalidTokenException("Refresh token not found"));
        if(!token.isRevoked()){
            tokenService.revoke(token);
        }
        log.info("User logged out :{}", token.getUser().getEmail());
    }

    @Transactional
    public void logoutAll(User user){
        tokenService.revokeAllByUser(user.getId());
        log.info("All sessions revoked for user :{}", user.getEmail());
    }


    private AuthResponse generateToken(User user, String deviceInfo, String ipAddress) {
        String accessToken = jwtService.generateAccessToken(
                user.getId(),
                user.getCompany() != null ? user.getCompany().getId() : null,
                user.getCompany() != null ? user.getCompany().getType().name() : null
        );
        String plainRefreshToken = tokenService.generateRefreshToken();
        tokenService.createAndSave(user, plainRefreshToken, deviceInfo, ipAddress);

        return new AuthResponse(
                accessToken,
                plainRefreshToken,
                jwtService.getAccessTokenExpiration()
        );
    }

}
