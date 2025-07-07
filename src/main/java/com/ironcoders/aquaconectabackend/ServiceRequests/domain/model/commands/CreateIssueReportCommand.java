package com.ironcoders.aquaconectabackend.ServiceRequests.domain.model.commands;

public record CreateIssueReportCommand(
    String title,
    String description,
    String status,
    Long userId
) {

}
