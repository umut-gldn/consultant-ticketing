package com.umutgldn.tickethub.project;

import com.umutgldn.tickethub.common.BaseEntity;
import com.umutgldn.tickethub.role.Role;
import com.umutgldn.tickethub.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "invitations")
@Getter
@Setter
@NoArgsConstructor
public class Invitation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id",nullable = false)
    private Project project;

    @Column(nullable = false)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id",nullable = false)
    private Role role;

    @Column(nullable = false,unique = true)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invited_by",nullable = false)
    private User invitedBy;

    @Column(name = "expires_at",nullable = false)
    private Instant expiresAt;

    @Column(name = "accepted_at")
    private Instant acceptedAt;

    public boolean isExpired() {
        return expiresAt.isBefore(Instant.now());
    }

    public boolean isAccepted() {
        return acceptedAt!=null;
    }

    public boolean isValid(){
        return !isExpired() && !isAccepted();
    }

}
