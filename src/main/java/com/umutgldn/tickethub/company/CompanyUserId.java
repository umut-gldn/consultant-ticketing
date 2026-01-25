package com.umutgldn.tickethub.company;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CompanyUserId implements Serializable {

    @Column(name = "company_id")
    private UUID companyId;

    @Column(name = "user_id")
    private UUID userId;
}
