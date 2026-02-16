package com.umutgldn.tickethub.project;

import com.umutgldn.tickethub.common.AuditableEntity;
import com.umutgldn.tickethub.role.Role;
import com.umutgldn.tickethub.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "project_users")
@Getter
@NoArgsConstructor
public class ProjectUser extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invited_by")
    private User invitedBy;

    @Column(name = "invited_at")
    private Instant invitedAt;

    @Column(name = "joined_at")
    private Instant joinedAt;

    @Column(name = "is_active")
    private boolean isActive = true;

    public ProjectUser(Project project, User user, Role role, User invitedBy) {
        this.project = project;
        this.user = user;
        this.role = role;
        this.invitedBy = invitedBy;
        this.invitedAt = Instant.now();
    }

    public boolean hasJoined() {
        return joinedAt != null;
    }

    public void join() {
        if (this.joinedAt != null) {
            throw new IllegalStateException("User has already joined the project");
        }
        this.joinedAt = Instant.now();
    }

    public void deactivate() {
        this.isActive = false;
    }

    public void activate() {
        this.isActive = true;
    }

    public void changeRole(Role newRole) {
        this.role = newRole;
    }

}
