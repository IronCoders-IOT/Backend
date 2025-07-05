package com.ironcoders.aquaconectabackend.profiles.domain.services;

import com.ironcoders.aquaconectabackend.profiles.domain.model.DTO.ResidentWithCredentials;
import com.ironcoders.aquaconectabackend.profiles.domain.model.aggregates.Resident;
import com.ironcoders.aquaconectabackend.profiles.domain.model.commands.CreateResidentCommand;
import com.ironcoders.aquaconectabackend.profiles.domain.model.commands.UpdateResidentCommand;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

public interface ResidentCommandService {

    ResidentWithCredentials handle(CreateResidentCommand command) throws AccessDeniedException;
    Optional<Resident> handle(UpdateResidentCommand command);

}
