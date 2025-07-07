package com.ironcoders.aquaconectabackend.monitoring.domain.model.commads;

public record CreateIssueReportCommand(
    String title,
    String description,
    String status,
    Long userId
) {

}
