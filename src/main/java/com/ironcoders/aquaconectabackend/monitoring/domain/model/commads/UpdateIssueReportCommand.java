package com.ironcoders.aquaconectabackend.monitoring.domain.model.commads;

public record UpdateIssueReportCommand(Long id,
    String status,
    Long userId
) {
}




