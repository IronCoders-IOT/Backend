package com.ironcoders.aquaconectabackend.monitoring.infrastructure.persistence.jpa.repositories;

import org.apache.coyote.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ironcoders.aquaconectabackend.monitoring.domain.model.aggregates.IssueReport;

import java.util.List;
import java.util.Optional;

@Repository
public interface IssueReportRepository extends JpaRepository<IssueReport, Long> {
    List<IssueReport> findByResidentId(Long residentId);
    List<IssueReport> findByProviderId(Long providerId);


}