package com.ironcoders.aquaconectabackend.management.domain.services;

import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.IssueReport;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetAllIssueReportsQuery;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetAllIsueReportsByProviderIdQuery;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetIssueReportByIdQuery;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetAllIssueReportsByResidentIdQuery;

import java.util.List;
import java.util.Optional;


public interface IssueReportQueryService {
    Optional<IssueReport> handle(GetIssueReportByIdQuery query);
    List<IssueReport> handle(GetAllIssueReportsByResidentIdQuery query);
    List<IssueReport> handle(GetAllIsueReportsByProviderIdQuery query);
    List<IssueReport> handle(GetAllIssueReportsQuery query);
}