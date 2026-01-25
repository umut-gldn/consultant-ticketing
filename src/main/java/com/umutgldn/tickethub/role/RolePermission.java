package com.umutgldn.tickethub.role;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "role_permissions")
@Getter
@Setter
@NoArgsConstructor
public class RolePermission {

    @EmbeddedId
    private RolePermissionId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roleId")
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("permissionId")
    @JoinColumn(name = "permission_id",nullable = false)
    private Permission permission;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    public RolePermission(Role role, Permission permission) {
        this.id=new RolePermissionId(role.getId(),permission.getId());
        this.role=role;
        this.permission=permission;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

}
