package com.ironcoders.aquaconectabackend.management.application.internal.queryservices;

import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.SensorAggregate;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetAllSensorsByResidentId;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetSensorByResidentId;
import com.ironcoders.aquaconectabackend.management.domain.services.SensorQueryService;
import com.ironcoders.aquaconectabackend.management.domain.services.WaterRequestQueryService;
import com.ironcoders.aquaconectabackend.management.infrastructure.persistence.jpa.repositories.SensorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SensorQueryServiceImpl implements SensorQueryService {

    private final SensorRepository sensorRepository;

    public SensorQueryServiceImpl(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    @Override
    public Optional<SensorAggregate> handle(GetSensorByResidentId query) {
        return sensorRepository.findByResidentId(query.residentId());
    }

    @Override
    public List<SensorAggregate> handle(GetAllSensorsByResidentId query) {
        return sensorRepository.findAllByResidentId(query.residentId());
    }
}
