package com.ironcoders.aquaconectabackend.ServiceRequests.interfaces.rest.resources;

public record CreateIssueReportResource(
        String title,
        String description,
        String status
) {}