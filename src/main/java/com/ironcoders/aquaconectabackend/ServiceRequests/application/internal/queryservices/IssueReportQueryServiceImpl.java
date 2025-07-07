package com.ironcoders.aquaconectabackend.ServiceRequests.application.internal.queryservices;
import org.springframework.stereotype.Service;

import com.ironcoders.aquaconectabackend.ServiceRequests.domain.model.aggregates.IssueReport;
import com.ironcoders.aquaconectabackend.ServiceRequests.domain.model.queries.GetAllIssueReportsByResidentIdQuery;
import com.ironcoders.aquaconectabackend.ServiceRequests.domain.model.queries.GetAllIssueReportsQuery;
import com.ironcoders.aquaconectabackend.ServiceRequests.domain.model.queries.GetAllIsueReportsByProviderIdQuery;
import com.ironcoders.aquaconectabackend.ServiceRequests.domain.model.queries.GetIssueReportByIdQuery;
import com.ironcoders.aquaconectabackend.ServiceRequests.domain.services.IssueReportQueryService;
import com.ironcoders.aquaconectabackend.ServiceRequests.infrastructure.persistence.jpa.repositories.IssueReportRepository;

import java.util.List;
import java.util.Optional;

@Service
public class IssueReportQueryServiceImpl implements IssueReportQueryService {

    private final IssueReportRepository requestRepository;

    public IssueReportQueryServiceImpl(IssueReportRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @Override
    public Optional<IssueReport> handle(GetIssueReportByIdQuery query) {
        return requestRepository.findById(query.requestId());
    }

    @Override
    public List<IssueReport> handle(GetAllIsueReportsByProviderIdQuery query) {
        return requestRepository.findByProviderId(query.providerId());
    }

    @Override
    public List<IssueReport> handle(GetAllIssueReportsQuery query) {
        return requestRepository.findAll();
    }

    @Override
    public List<IssueReport> handle(GetAllIssueReportsByResidentIdQuery query) {
        return requestRepository.findByResidentId(query.residentId());
    }
}
