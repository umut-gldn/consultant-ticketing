package com.umutgldn.tickethub.company;

import com.umutgldn.tickethub.common.AuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "companies")
@Getter
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
        CONSULTANT,
        CLIENT
    }

    public Company(String name, String email, String phone, CompanyType type) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.type = type;
    }

    public boolean isConsultant(){
        return this.type==CompanyType.CONSULTANT;
    }

    public boolean isClient(){
        return this.type==CompanyType.CLIENT;
    }

    public void deactivate(){
        this.isActive=false;
    }

    public void activate(){
        this.isActive=true;
    }

    public void updateInfo(String email, String phone,String name){
        this.name=name;
        this.email=email;
        this.phone=phone;
    }

}
