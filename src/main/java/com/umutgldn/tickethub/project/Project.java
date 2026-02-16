package com.umutgldn.tickethub.project;

import com.umutgldn.tickethub.common.AuditableEntity;
import com.umutgldn.tickethub.company.Company;
import com.umutgldn.tickethub.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "projects")
@Getter
@NoArgsConstructor
public class Project extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consultant_company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_company_id", nullable = false)
    private Company clientCompany;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug;

    private String description;

    @Column(name = "is_active")
    private boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    public Project(Company consultantCompany, Company clientCompany, String name, String slug, String description, User createdBy) {
        this.company = consultantCompany;
        this.clientCompany = clientCompany;
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.createdBy = createdBy;
    }

    public boolean belongsToConsultant(Company consultant) {
        return this.company.getId().equals(consultant.getId());
    }

    public boolean belongsToClient(Company clientCompany) {
        return this.clientCompany.getId().equals(clientCompany.getId());
    }

    public void deactivate() {
        this.isActive = false;
    }

    public void activate() {
        this.isActive = true;
    }

    public void updateInfo(String name, String description){
        this.name = name;
        this.description = description;
    }

}
