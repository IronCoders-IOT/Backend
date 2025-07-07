package com.ironcoders.aquaconectabackend.ServiceRequests.interfaces.rest.resources;

public record IssueReportResource(
        Long id,
        String title,
        String description,
        String emissionDate,
        String status,
        Long residentId,
        Long providerId
) {}