package com.umutgldn.tickethub.user;

import com.umutgldn.tickethub.common.AuditableEntity;
import com.umutgldn.tickethub.company.Company;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table(name = "users")
@Getter
@Setter
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

    @Column(name ="is_active")
    private boolean isActive=true;

    @Column(name = "email_verified")
    private boolean isEmailVerified=false;

    @Column(name = "failed_login_attempts")
    private int failedLoginAttempts=0;

    @Column(name = "locked_until")
    private Instant lockedUntil;

    @Column(name = "last_login_at")
    private Instant lastLoginAt;

    //Helper Method
    public String getFullName(){
        return Stream.of(firstName,lastName)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" "));
    }

    public boolean isLocked(Instant now){
        return lockedUntil != null && lockedUntil.isAfter(now);
    }
}
