package com.ironcoders.aquaconectabackend.management.domain.services;

import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.WaterSupplyRequest;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.CreateWaterSupplyRequestCommand;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.UpdateWaterSupplyRequestCommand;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

public interface WaterSupplyRequestCommandService {
    Optional<WaterSupplyRequest> handle(CreateWaterSupplyRequestCommand command) throws AccessDeniedException;
    Optional<WaterSupplyRequest>handle(UpdateWaterSupplyRequestCommand command) throws AccessDeniedException;
}