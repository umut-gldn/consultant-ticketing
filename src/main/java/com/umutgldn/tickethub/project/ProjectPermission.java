package com.umutgldn.tickethub.project;

import com.umutgldn.tickethub.common.BaseEntity;
import com.umutgldn.tickethub.role.Permission;
import com.umutgldn.tickethub.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "project_user_permissions")
@Getter
@NoArgsConstructor
public class ProjectPermission extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_user_id", nullable = false)
    private ProjectUser projectUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_id", nullable = false)
    private Permission permission;

    @Column(name = "is_granted")
    private boolean isGranted = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "granted_by", nullable = false)
    private User grantedBy;

    public ProjectPermission(ProjectUser projectUser, Permission permission, boolean isGranted, User grantedBy) {
        this.projectUser = projectUser;
        this.permission = permission;
        this.isGranted = isGranted;
        this.grantedBy = grantedBy;
    }

    public void grant(User grantedBy) {
        this.isGranted = true;
        this.grantedBy = grantedBy;
    }

    public void revoke(User revokedBy) {
        this.isGranted = false;
        this.grantedBy = revokedBy;
    }

}
