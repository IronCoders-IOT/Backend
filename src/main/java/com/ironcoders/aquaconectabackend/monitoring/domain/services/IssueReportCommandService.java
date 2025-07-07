package com.ironcoders.aquaconectabackend.monitoring.domain.services;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

import com.ironcoders.aquaconectabackend.monitoring.domain.model.aggregates.IssueReport;
import com.ironcoders.aquaconectabackend.monitoring.domain.model.commads.CreateIssueReportCommand;
import com.ironcoders.aquaconectabackend.monitoring.domain.model.commads.UpdateIssueReportCommand;

public interface IssueReportCommandService {
  Optional<IssueReport> handle(CreateIssueReportCommand command) throws AccessDeniedException;
  Optional<IssueReport> handle(UpdateIssueReportCommand command) throws AccessDeniedException;
}
