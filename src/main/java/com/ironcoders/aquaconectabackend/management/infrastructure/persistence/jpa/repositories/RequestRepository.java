package com.ironcoders.aquaconectabackend.management.infrastructure.persistence.jpa.repositories;

import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.RequestAggregate;
import org.apache.coyote.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<RequestAggregate, Long> {
    Optional<RequestAggregate> findById(Long id);
    List<RequestAggregate> findAll();
    List<RequestAggregate> findByResidentId(Long residentId);
}