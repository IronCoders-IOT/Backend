package com.ironcoders.aquaconectabackend.management.infrastructure.persistence.jpa.repositories;

import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.IssueReport;
import org.apache.coyote.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IssueReportRepository extends JpaRepository<IssueReport, Long> {
    List<IssueReport> findByResidentId(Long residentId);
    List<IssueReport> findByProviderId(Long providerId);


}