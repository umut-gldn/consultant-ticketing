package com.umutgldn.tickethub.auth.service;

import com.umutgldn.tickethub.auth.config.JwtProperties;
import com.umutgldn.tickethub.auth.entity.RefreshToken;
import com.umutgldn.tickethub.auth.repository.RefreshTokenRepository;
import com.umutgldn.tickethub.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtProperties jwtProperties;
    private final RefreshTokenRepository refreshTokenRepository;

    public String generateRefreshToken(){
        return UUID.randomUUID().toString();
    }

    @Transactional
    public RefreshToken createAndSave(User user, String plainToken, String deviceInfo, String ipAddress){
        Instant expires= Instant.now().plusMillis(jwtProperties.refreshTokenExpiration());
        RefreshToken refreshToken= new RefreshToken(user,hashToken(plainToken),expires,deviceInfo,ipAddress);
        return refreshTokenRepository.save(refreshToken);
    }

    @Transactional(readOnly = true)//olmasa da olur ama  bu method veri degistirmeyecegi garantisi veriyorum.
    public Optional<RefreshToken> findByPlainToken(String plainToken){
        return refreshTokenRepository.findByToken(hashToken(plainToken));
    }

    @Transactional
    public void revoke(RefreshToken token){
        token.revoke(Instant.now());
        refreshTokenRepository.save(token);
    }

    @Transactional
    public void revokeAllByUser(UUID userId){
        refreshTokenRepository.revokeAllByUserId(userId);
    }

    private String hashToken(String token){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash=digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        }
        catch (NoSuchAlgorithmException e){
            throw new IllegalStateException("SHA-256 algorithm not available",e);
        }
    }
}
