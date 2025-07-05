package com.ironcoders.aquaconectabackend.management.domain.services;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.IssueReport;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.CreateIssueReportCommand;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.UpdateIssueReportCommand;

public interface IssueReportCommandService {
  Optional<IssueReport> handle(CreateIssueReportCommand command) throws AccessDeniedException;
  Optional<IssueReport> handle(UpdateIssueReportCommand command) throws AccessDeniedException;
}
