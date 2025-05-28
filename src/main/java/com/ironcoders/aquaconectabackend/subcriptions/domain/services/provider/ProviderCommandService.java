package com.ironcoders.aquaconectabackend.subcriptions.domain.services.provider;

import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Provider;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.provider.CreateProviderCommand;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.provider.UpdateProviderCommand;

import java.util.Optional;

public interface ProviderCommandService {

    Optional<Provider> handle(CreateProviderCommand command);
    Optional<Provider> handle(UpdateProviderCommand command);

}
