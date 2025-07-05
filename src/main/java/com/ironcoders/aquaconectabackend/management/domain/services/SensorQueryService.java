package com.ironcoders.aquaconectabackend.management.domain.services;

import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.Device;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetAllDevicesByResidentId;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetDeviceByIdQuery;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetDeviceByResidentId;

import java.util.List;
import java.util.Optional;

public interface SensorQueryService {

    Optional<Device> handle(GetDeviceByResidentId query);
    List<Device> handle(GetAllDevicesByResidentId query);


    Optional<Device>handle(GetDeviceByIdQuery query);

}
