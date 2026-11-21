package com.saga2.dealer_service.entity;

import com.saga2.dealer_service.enums.DealerStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "dealers", schema = "dealer_schema")
@Getter
@Setter
public class Dealer {


    @Id
    @Column(name = "dealer_id", length = 20)
    private String dealerId;

    @Column(name = "dealer_name", nullable = false, length = 150)
    private String dealerName;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(nullable = false, length = 100)
    private String city;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DealerStatus status = DealerStatus.ACTIVE;

    @Column(name = "created_at", nullable = false,
            updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @PrePersist
    public void onCreate(){

        this.createdAt = OffsetDateTime.now();;
        this.updatedAt = OffsetDateTime.now();;
    }

    public void onUpdate(){
        this.updatedAt = OffsetDateTime.now();
    }


}
