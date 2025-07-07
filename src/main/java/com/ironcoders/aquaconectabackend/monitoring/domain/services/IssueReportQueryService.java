package com.ironcoders.aquaconectabackend.monitoring.domain.services;

import java.util.List;
import java.util.Optional;

import com.ironcoders.aquaconectabackend.monitoring.domain.model.aggregates.IssueReport;
import com.ironcoders.aquaconectabackend.monitoring.domain.model.queries.GetAllIssueReportsByResidentIdQuery;
import com.ironcoders.aquaconectabackend.monitoring.domain.model.queries.GetAllIssueReportsQuery;
import com.ironcoders.aquaconectabackend.monitoring.domain.model.queries.GetAllIsueReportsByProviderIdQuery;
import com.ironcoders.aquaconectabackend.monitoring.domain.model.queries.GetIssueReportByIdQuery;


public interface IssueReportQueryService {
    Optional<IssueReport> handle(GetIssueReportByIdQuery query);
    List<IssueReport> handle(GetAllIssueReportsByResidentIdQuery query);
    List<IssueReport> handle(GetAllIsueReportsByProviderIdQuery query);
    List<IssueReport> handle(GetAllIssueReportsQuery query);
}