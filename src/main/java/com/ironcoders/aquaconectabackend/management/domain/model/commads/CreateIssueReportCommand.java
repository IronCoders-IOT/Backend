package com.ironcoders.aquaconectabackend.management.domain.model.commads;

public record CreateIssueReportCommand(
    String title,
    String description,
    String status,
    Long userId
) {

}
