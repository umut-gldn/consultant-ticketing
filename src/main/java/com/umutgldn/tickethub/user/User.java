package com.umutgldn.tickethub.user;

import com.umutgldn.tickethub.common.AuditableEntity;
import com.umutgldn.tickethub.company.Company;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class User extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "is_active")
    private boolean isActive = true;

    @Column(name = "email_verified")
    private boolean isEmailVerified = false;

    @Column(name = "failed_login_attempts")
    private int failedLoginAttempts = 0;

    @Column(name = "lock_count")
    private int lockCount = 0;

    @Column(name = "locked_until")
    private Instant lockedUntil;

    @Column(name = "last_login_at")
    private Instant lastLoginAt;

    public User(Company company, String email, String passwordHash, String firstName, String lastName) {
        this.company = company;
        this.email = email;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    //Helper Method
    public String getFullName() {
        return Stream.of(firstName, lastName)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" "));
    }

    public boolean isLocked(Instant now) {
        return lockedUntil != null && lockedUntil.isAfter(now);
    }

    public boolean isPermanentlyLocked(int maxLockCount) {
        return lockCount >=maxLockCount;
    }

    //behavior methods
    /**
     * Başarısız giriş denemesi kaydeder.
     * maxFailedAttempts'a ulaşıldığında progressive lock uygulanır.
     * maxLockCount'a ulaşıldığında hesap devre dışı bırakılır.
     *
     * @param now               şu anki zaman
     * @param maxFailedAttempts kilitlenme için gereken başarısız deneme sayısı
     * @param maxLockCount      hesap devre dışı bırakılmadan önce max kilit sayısı
     * @param lockDuration      bu kilit seviyesi için uygulanacak süre
     */
    public void recordFailedLogin(Instant now,int maxFailedAttempts,int maxLockCount,Duration lockDuration) {
        this.failedLoginAttempts++;

        if (this.failedLoginAttempts >= maxFailedAttempts) {
            if (this.lockCount >= maxLockCount) {
                this.isActive = false;
                return;
            }

            this.lockedUntil = now.plus(lockDuration);
            this.lockCount++;
            this.failedLoginAttempts = 0;
        }
    }

    public void onLoginSuccess(Instant now) {
        this.failedLoginAttempts = 0;
        this.lockedUntil = null;
        this.lastLoginAt = now;
    }

    public void resetLockHistory() {
        this.failedLoginAttempts = 0;
        this.lockCount = 0;
        this.lockedUntil = null;
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }

    public void activate() {
        this.isActive = true;
    }

    public void verifyEmail() {
        this.isEmailVerified = true;
    }

    public void changePassword(String newPassword) {
        this.passwordHash = newPassword;
    }

    public void updateProfile(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void assignCompany(Company company) {
        this.company = company;
    }
}
