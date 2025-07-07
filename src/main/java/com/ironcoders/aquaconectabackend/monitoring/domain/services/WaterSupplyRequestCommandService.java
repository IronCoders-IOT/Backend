package com.ironcoders.aquaconectabackend.monitoring.domain.services;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

import com.ironcoders.aquaconectabackend.monitoring.domain.model.aggregates.WaterSupplyRequest;
import com.ironcoders.aquaconectabackend.monitoring.domain.model.commads.CreateWaterSupplyRequestCommand;
import com.ironcoders.aquaconectabackend.monitoring.domain.model.commads.UpdateWaterSupplyRequestCommand;

public interface WaterSupplyRequestCommandService {
    Optional<WaterSupplyRequest> handle(CreateWaterSupplyRequestCommand command) throws AccessDeniedException;
    Optional<WaterSupplyRequest>handle(UpdateWaterSupplyRequestCommand command) throws AccessDeniedException;
}