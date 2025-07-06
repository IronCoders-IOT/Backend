package com.ironcoders.aquaconectabackend.management.application.internal.queryservices;

import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.Device;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetAllDevicesByResidentId;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetDeviceByIdQuery;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetDeviceByResidentId;
import com.ironcoders.aquaconectabackend.management.domain.services.DeviceQueryService;
import com.ironcoders.aquaconectabackend.management.infrastructure.persistence.jpa.repositories.DeviceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeviceQueryServiceImpl implements DeviceQueryService {

    private final DeviceRepository sensorRepository;

    public DeviceQueryServiceImpl(DeviceRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    @Override
    public Optional<Device> handle(GetDeviceByResidentId query) {
        return sensorRepository.findByResidentId(query.residentId());
    }

    @Override
    public List<Device> handle(GetAllDevicesByResidentId query) {
        return sensorRepository.findAllByResidentId(query.residentId());
    }


    @Override
    public Optional<Device>handle(GetDeviceByIdQuery query){
        return sensorRepository.findById(query.sensorId());
    }
}
