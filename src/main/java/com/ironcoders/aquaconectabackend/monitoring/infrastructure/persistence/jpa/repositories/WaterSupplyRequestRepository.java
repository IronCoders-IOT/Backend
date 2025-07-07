package com.ironcoders.aquaconectabackend.monitoring.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ironcoders.aquaconectabackend.monitoring.domain.model.aggregates.WaterSupplyRequest;

import java.util.List;

@Repository
public interface WaterSupplyRequestRepository extends JpaRepository<WaterSupplyRequest, Long> {
    List<WaterSupplyRequest> findByResidentId(Long residentId);
    List<WaterSupplyRequest> findByProviderId(Long providerId);
}