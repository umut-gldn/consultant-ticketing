package com.umutgldn.tickethub.auth;

import com.umutgldn.tickethub.auth.exception.InvalidTokenException;
import com.umutgldn.tickethub.auth.exception.TokenExpiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class JwtService {

    private final JwtProperties jwtProperties;
    private final SecretKey signingKey;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.signingKey= Keys.hmacShaKeyFor(jwtProperties.secret().getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(UUID userId, UUID companyId, String companyType) {
        Instant now = Instant.now();
        Instant expiry = now.plusMillis(jwtProperties.accessTokenExpiration());

        var builder= Jwts.builder()
                .subject(userId.toString())
                .claim("companyType", companyType)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry));

        if(companyId != null) {
            builder.claim("companyId", companyId.toString());
        }
        return builder
                .signWith(signingKey)
                .compact();
    }

    public Claims parseToken(String token) {
        try
        { return Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        }catch(ExpiredJwtException e){
            throw new TokenExpiredException("Token expired");
        } catch (JwtException e) {
            throw new InvalidTokenException("Invalid token");
        }
    }

    public UUID extractUserId(Claims claims) {
        return UUID.fromString(claims.getSubject());
    }

    public UUID extractCompanyId(Claims claims) {
        String companyId = claims.get("companyId", String.class);
        return companyId != null ? UUID.fromString(companyId) : null;
    }

    public String extractCompanyType(Claims claims) {
        return claims.get("companyType", String.class);
    }

    public long getAccessTokenExpiration() {
        return jwtProperties.accessTokenExpiration();
    }
}
