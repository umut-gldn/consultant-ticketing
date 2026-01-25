package com.umutgldn.tickethub.company;

import com.umutgldn.tickethub.common.AuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "companies")
@Getter
@Setter
@NoArgsConstructor
public class Company extends AuditableEntity {

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CompanyType type;

    private String email;

    private String phone;

    @Column(name = "is_active")
    private boolean isActive=true;
    
    public enum CompanyType {
        consultant, client
    }

}
