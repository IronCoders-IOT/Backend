package com.ironcoders.aquaconectabackend.ServiceRequests.interfaces.rest.transform;

import com.ironcoders.aquaconectabackend.ServiceRequests.domain.model.commands.CreateIssueReportCommand;
import com.ironcoders.aquaconectabackend.ServiceRequests.interfaces.rest.resources.CreateIssueReportResource;
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