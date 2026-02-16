package com.umutgldn.tickethub.auth.entity;

import com.umutgldn.tickethub.common.BaseEntity;
import com.umutgldn.tickethub.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "refresh_tokens")
@Getter
@NoArgsConstructor
public class RefreshToken extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @Column(nullable = false,unique = true)
    private String token;

    @Column(name = "expires_at",nullable = false)
    private Instant expiresAt;

    @Column(name = "revoked_at")
    private Instant revokedAt;

    @Column(name = "device_info")
    private String deviceInfo;

    @Column(name = "ip_address")
    private String ipAddress;

    public RefreshToken(User user, String token, Instant expiresAt, String deviceInfo, String ipAddress) {
        this.user = user;
        this.token = token;
        this.expiresAt = expiresAt;
        this.deviceInfo = deviceInfo;
        this.ipAddress = ipAddress;
    }
    //entity's behavior
    public boolean isExpired(Instant now){
        return expiresAt.isBefore(now);
    }

    public boolean isRevoked(){
        return revokedAt !=null;
    }

    public boolean isValid(Instant now){
        return !isExpired(now) && !isRevoked();
    }

    public void revoke(Instant now){
        if(this.revokedAt!=null){
            throw new IllegalStateException("Token is already revoked");
        }
        this.revokedAt=now;
    }

}
