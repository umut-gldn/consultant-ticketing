package com.umutgldn.tickethub.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.List;

@ConfigurationProperties(prefix = "security.login")
public record LoginSecurityProperties(
        int maxFailedAttempts,
        int maxLockCount,
        List<Duration> lockDurations
) {
    public LoginSecurityProperties{
        if(lockDurations.size() !=maxLockCount){
             throw new IllegalArgumentException(
                    "lockDurations size (%d) must equal maxLockCount (%d)"
                            .formatted(lockDurations.size(), maxLockCount));
        }
    }

    public Duration getLockDuration(int lockCount){
        return lockDurations.get(lockCount);
    }
}
