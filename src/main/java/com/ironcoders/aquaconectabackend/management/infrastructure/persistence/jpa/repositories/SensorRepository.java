package com.ironcoders.aquaconectabackend.management.infrastructure.persistence.jpa.repositories;

import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.SensorAggregate;
import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.WaterRequestAggregate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SensorRepository extends JpaRepository<SensorAggregate, Long> {

    Optional<SensorAggregate> findByResidentId(Long residentId);
    List<SensorAggregate> findAllByResidentId(Long residentId);
}
