package com.ironcoders.aquaconectabackend.management.infrastructure.persistence.jpa.repositories;

import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.WaterRequestAggregate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WaterRequestRepository extends JpaRepository<WaterRequestAggregate, Long> {
    List<WaterRequestAggregate> findByResidentId(Long residentId);
    List<WaterRequestAggregate> findByProviderId(Long providerId);
}