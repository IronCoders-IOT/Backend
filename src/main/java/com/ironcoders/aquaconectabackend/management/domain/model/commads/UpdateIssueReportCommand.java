package com.ironcoders.aquaconectabackend.management.domain.model.commads;

public record UpdateIssueReportCommand(Long id,
    String status,
    Long userId
) {
}




