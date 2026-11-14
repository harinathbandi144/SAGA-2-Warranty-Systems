package com.saga.claim_service.entity;

import com.saga.claim_service.enums.Status;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Entity
@Setter
@Getter
@Table(name = "claims", schema = "claim_schema")
@NoArgsConstructor
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "claim_number", unique = true, length = 50)
    private String claimNumber;

    @Column(name = "dealer_id", nullable = false, length = 50)
    private String dealerId;

    @Column(name = "vehicle_vin", nullable = false, length = 17)
    private String vin;

    @Column(name = "failure_description", nullable = false, length = 1000)
    private String failureDescription;

    @Column(name = "claimed_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal claimedAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private Status status = Status.DRAFT;


    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @PrePersist
    public void onCreate(){
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void onUpdate(){
        this.updatedAt = OffsetDateTime.now(ZoneOffset.UTC);
    }








}
