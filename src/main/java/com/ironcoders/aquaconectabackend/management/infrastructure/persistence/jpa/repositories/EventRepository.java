package com.ironcoders.aquaconectabackend.management.infrastructure.persistence.jpa.repositories;

import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findBySensorId(Long sensorId);
}