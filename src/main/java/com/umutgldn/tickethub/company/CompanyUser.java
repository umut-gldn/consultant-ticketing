package com.umutgldn.tickethub.company;

import com.umutgldn.tickethub.role.Role;
import com.umutgldn.tickethub.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "company_users")
@Getter
@NoArgsConstructor
public class CompanyUser {

    @EmbeddedId
    private CompanyUserId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("companyId")
    @JoinColumn(name = "company_id",nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id",nullable = false)
    private Role role;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    public CompanyUser(Company company, User user, Role role) {
        this.id = new CompanyUserId(company.getId(), user.getId());
        this.company = company;
        this.user = user;
        this.role = role;
    }

    @PrePersist
    protected void onCreate(){
        this.createdAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = Instant.now();
    }
}
