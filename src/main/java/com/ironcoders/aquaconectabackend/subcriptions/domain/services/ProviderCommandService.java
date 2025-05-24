package com.ironcoders.aquaconectabackend.subcriptions.domain.services;

import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Provider;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.CreateProviderCommand;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.UpdateProviderCommand;

import java.util.Optional;

public interface ProviderCommandService {

    Optional<Provider> handle(CreateProviderCommand command);
    Optional<Provider> handle(UpdateProviderCommand command);

}
