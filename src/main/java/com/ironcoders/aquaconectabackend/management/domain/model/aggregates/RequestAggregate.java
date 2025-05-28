package com.ironcoders.aquaconectabackend.management.domain.model.aggregates;

import com.ironcoders.aquaconectabackend.management.domain.model.commads.CreateRequestCommand;
import com.ironcoders.aquaconectabackend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
@Entity
public class RequestAggregate extends AuditableAbstractAggregateRoot<RequestAggregate> {

    

    @Column(nullable = false)
    private Long residentId;

    @Column(nullable = false)
    private Long providerId;

    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false)
    private String description;
    
    @Column(nullable = false)
    private String status;

    public RequestAggregate() {}

    public RequestAggregate(Long residentId, Long providerId, String title, String description, String status) {
        this.residentId = residentId;
        this.providerId = providerId;
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public RequestAggregate(CreateRequestCommand command) {
        this.residentId = command.residentId();
        this.providerId = command.providerId();
        this.title = command.title();
        this.description = command.description();
        this.status = command.status();
    }


    //    public EventAggregate(CreateEventCommand command){
    //    this.eventType= command.eventType();
    //    this.qualityValue = command.qualityValue();
    //    this.levelValue = command.levelValue();
    //    this.sensorId = command.sensorId();
   // }
    //
    
}
