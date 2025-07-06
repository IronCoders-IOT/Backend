package com.ironcoders.aquaconectabackend.management.application.internal.commandservices;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.Device;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.CreateDeviceCommand;
import com.ironcoders.aquaconectabackend.management.domain.services.DeviceCommandService;
import com.ironcoders.aquaconectabackend.management.infrastructure.persistence.jpa.repositories.DeviceRepository;

@Service
public class DeviceCommandServiceImpl implements DeviceCommandService {

    private final DeviceRepository deviceRepository;

    public DeviceCommandServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public Optional<Device> handle(CreateDeviceCommand command) {

        var device = new Device(command);
        deviceRepository.save(device);
        return Optional.of(device);
    }



   
   
    
}
