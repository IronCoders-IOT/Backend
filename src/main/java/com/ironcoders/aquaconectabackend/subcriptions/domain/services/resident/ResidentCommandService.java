package com.ironcoders.aquaconectabackend.subcriptions.domain.services.resident;

import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Provider;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Resident;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.ResidentWithCredentials;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.provider.CreateProviderCommand;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.provider.UpdateProviderCommand;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.resident.CreateResidentCommand;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.resident.UpdateResidentCommand;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

public interface ResidentCommandService {

    ResidentWithCredentials handle(CreateResidentCommand command) throws AccessDeniedException;
    Optional<Resident> handle(UpdateResidentCommand command);

}
