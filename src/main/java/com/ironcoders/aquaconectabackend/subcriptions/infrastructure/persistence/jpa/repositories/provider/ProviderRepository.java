package com.ironcoders.aquaconectabackend.subcriptions.infrastructure.persistence.jpa.repositories.provider;


import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {
    List<Provider> findByUserId(Long userId);
}