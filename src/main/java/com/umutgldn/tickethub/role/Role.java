package com.umutgldn.tickethub.role;

import com.umutgldn.tickethub.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
public class Role extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleScope scope;

    @Column(name = "is_system_role")
    private boolean isSystemRole=true;

    public enum RoleScope {
        company,project
    }

}
