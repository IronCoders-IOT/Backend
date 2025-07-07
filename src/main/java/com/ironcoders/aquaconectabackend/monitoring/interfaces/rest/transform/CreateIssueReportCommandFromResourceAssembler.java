package com.ironcoders.aquaconectabackend.monitoring.interfaces.rest.transform;

import com.ironcoders.aquaconectabackend.monitoring.domain.model.commads.CreateIssueReportCommand;
import com.ironcoders.aquaconectabackend.monitoring.interfaces.rest.resources.CreateIssueReportResource;
public class CreateIssueReportCommandFromResourceAssembler {

    public static CreateIssueReportCommand toCommandFromResource(CreateIssueReportResource resource, Long userId) {
        return new CreateIssueReportCommand(
                resource.title(),
                resource.description(),
                resource.status(),
                userId
                

        );
    }

}