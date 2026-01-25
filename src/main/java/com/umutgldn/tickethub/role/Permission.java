package com.umutgldn.tickethub.role;

import com.umutgldn.tickethub.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "permissions")
@Getter
@Setter
@NoArgsConstructor
public class Permission extends BaseEntity {

    @Column(nullable = false,unique = true)
    private String name;

    @Column(nullable = false)
    private String resource;

    @Column(nullable = false)
    private String action;

    private String description;
}
