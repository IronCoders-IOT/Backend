package com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates;

import com.ironcoders.aquaconectabackend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.provider.CreateProviderCommand;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.provider.UpdateProviderCommand;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;

@Getter
@Entity
public class Provider extends AuditableAbstractAggregateRoot<Provider> {
    @Column(nullable = false)
    private String taxName;
    @Column(nullable = false)
    private String ruc;
    @Column(nullable = false)
    private Long userId;

    public Provider(String taxName, String ruc, Long userId) {
        this.taxName = taxName;
        this.ruc = ruc;
        this.userId = userId;
    }

    public Provider(CreateProviderCommand command, Long userId){
        this.taxName= command.taxName();
        this.ruc= command.ruc();
        this.userId= userId;

    }

    public Provider() {

    }

    public void update(UpdateProviderCommand command) {
        this.taxName = command.taxName();
        this.ruc = command.ruc();
    }




}
