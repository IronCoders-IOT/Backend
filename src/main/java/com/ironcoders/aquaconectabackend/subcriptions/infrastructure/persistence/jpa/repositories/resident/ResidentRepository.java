package com.ironcoders.aquaconectabackend.subcriptions.infrastructure.persistence.jpa.repositories.resident;

import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Provider;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Resident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResidentRepository extends JpaRepository<Resident, Long> {
    List<Resident> findByUserId(Long userId);
    List<Resident> findByProviderId(Long providerId);

}
