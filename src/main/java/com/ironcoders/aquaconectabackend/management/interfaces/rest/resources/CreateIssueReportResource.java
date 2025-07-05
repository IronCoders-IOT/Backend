package com.ironcoders.aquaconectabackend.management.interfaces.rest.resources;

public record CreateIssueReportResource(
        String title,
        String description,
        String status
) {}