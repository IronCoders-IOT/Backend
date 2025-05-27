package com.ironcoders.aquaconectabackend.management.domain.model.aggregates;

import com.ironcoders.aquaconectabackend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
@Entity
@Table(name = "requests")
public class RequestAggregate extends AuditableAbstractAggregateRoot<RequestAggregate> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idRequest;

    @NotNull
    @Column(name = "resident_id")
    private Long residentId;

    @NotNull
    @Column(name = "provider_id")
    private Long providerId;

    @NotBlank
    @Size(max = 255)
    private String title;

    @NotBlank
    @Size(max = 255)
    private String description;

    @NotBlank
    @Size(max = 50)
    private String status;

    public RequestAggregate() {}

    public RequestAggregate(Long residentId, Long providerId, String title, String description, String status) {
        this.residentId = residentId;
        this.providerId = providerId;
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public RequestAggregate update(String title, String description, String status) {
        this.title = title;
        this.description = description;
        this.status = status;
        return this;
    }
}