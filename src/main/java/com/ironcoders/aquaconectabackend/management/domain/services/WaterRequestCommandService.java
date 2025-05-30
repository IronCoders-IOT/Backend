package com.ironcoders.aquaconectabackend.management.domain.services;

import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.WaterRequestAggregate;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.CreateWaterRequestCommand;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.UpdateWaterRequestCommand;

import java.util.Optional;

public interface WaterRequestCommandService {
    Optional<WaterRequestAggregate> handle(CreateWaterRequestCommand command);
    Optional<WaterRequestAggregate>handle(UpdateWaterRequestCommand command);
}