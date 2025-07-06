package com.ironcoders.aquaconectabackend.management.domain.services;

import java.util.Optional;

import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.Device;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.CreateDeviceCommand;

public interface DeviceCommandService {
    Optional<Device> handle(CreateDeviceCommand command);

}
