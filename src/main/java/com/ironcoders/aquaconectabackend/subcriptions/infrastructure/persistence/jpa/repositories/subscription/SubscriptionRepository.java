package com.ironcoders.aquaconectabackend.subcriptions.infrastructure.persistence.jpa.repositories.subscription;

import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription,Long> {

    List<Subscription> findByResidentId(Long residentId);
}
