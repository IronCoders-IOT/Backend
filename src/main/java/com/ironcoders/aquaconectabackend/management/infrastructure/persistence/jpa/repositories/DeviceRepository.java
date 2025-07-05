package com.ironcoders.aquaconectabackend.management.infrastructure.persistence.jpa.repositories;

import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.Device;
import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.WaterSupplyRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, Long> {

    Optional<Device> findByResidentId(Long residentId);
    List<Device> findAllByResidentId(Long residentId);
}
