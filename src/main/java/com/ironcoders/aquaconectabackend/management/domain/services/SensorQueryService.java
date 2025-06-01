package com.ironcoders.aquaconectabackend.management.domain.services;

import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.SensorAggregate;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetAllSensorsByResidentId;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetSensorByResidentId;

import java.util.List;
import java.util.Optional;

public interface SensorQueryService {

    Optional<SensorAggregate> handle(GetSensorByResidentId query);
    List<SensorAggregate> handle(GetAllSensorsByResidentId query);


}
