package com.ironcoders.aquaconectabackend.management.interfaces.rest.transform;

import com.ironcoders.aquaconectabackend.management.domain.model.commads.CreateIssueReportCommand;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.CreateIssueReportResource;
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