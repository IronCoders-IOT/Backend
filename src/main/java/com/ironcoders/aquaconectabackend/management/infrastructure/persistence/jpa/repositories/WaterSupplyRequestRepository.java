package com.ironcoders.aquaconectabackend.management.infrastructure.persistence.jpa.repositories;

import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.WaterSupplyRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WaterSupplyRequestRepository extends JpaRepository<WaterSupplyRequest, Long> {
    List<WaterSupplyRequest> findByResidentId(Long residentId);
    List<WaterSupplyRequest> findByProviderId(Long providerId);
}