package com.ironcoders.aquaconectabackend.management.interfaces.rest.transform;

import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.IssueReport;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.IssueReportResource;

public class IssueReportResourceFromEntityAssembler {
    public static IssueReportResource toResourceFromEntity(IssueReport aggregate) {
        return new IssueReportResource(
            aggregate.getId(),
            aggregate.getTitle(),
                aggregate.getDescription(),
                aggregate.getEmissionDate(),
                aggregate.getStatus(),
                aggregate.getResidentId(),
                aggregate.getProviderId()
        );
    }
}