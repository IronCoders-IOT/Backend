package com.ironcoders.aquaconectabackend.management.domain.services;

import com.ironcoders.aquaconectabackend.management.domain.model.commads.CreateWaterRequestCommand;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.DeleteWaterRequestCommand;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.UpdateWaterRequestCommand;

public interface WaterRequestCommandService {
    void handle(CreateWaterRequestCommand command);
    void handle(UpdateWaterRequestCommand command);
    void handle(DeleteWaterRequestCommand command);
}