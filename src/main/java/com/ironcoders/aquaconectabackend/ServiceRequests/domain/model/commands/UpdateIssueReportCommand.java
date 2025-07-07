package com.ironcoders.aquaconectabackend.ServiceRequests.domain.model.commands;

public record UpdateIssueReportCommand(Long id,
    String status,
    Long userId
) {
}




