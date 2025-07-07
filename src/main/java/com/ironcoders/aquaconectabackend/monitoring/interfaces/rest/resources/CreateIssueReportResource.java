package com.ironcoders.aquaconectabackend.monitoring.interfaces.rest.resources;

public record CreateIssueReportResource(
        String title,
        String description,
        String status
) {}