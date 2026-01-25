package com.umutgldn.tickethub.common;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@MappedSuperclass
@Getter
@Setter
public abstract class AuditableEntity extends BaseEntity {
    @Column(name = "updated_at",nullable = false)
    private Instant updatedAt;

    @Override
    protected void onCreate() {
        super.onCreate();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}
