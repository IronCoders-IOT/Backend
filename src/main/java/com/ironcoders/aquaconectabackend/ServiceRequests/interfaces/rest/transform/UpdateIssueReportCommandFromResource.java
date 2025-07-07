package com.ironcoders.aquaconectabackend.ServiceRequests.interfaces.rest.transform;

import com.ironcoders.aquaconectabackend.ServiceRequests.domain.model.commands.UpdateIssueReportCommand;
import com.ironcoders.aquaconectabackend.ServiceRequests.interfaces.rest.resources.UpdateIssueReportResource;

public class UpdateIssueReportCommandFromResource {

    public static UpdateIssueReportCommand toCommandFromResource(Long id, UpdateIssueReportResource resource, Long userId) {
        return new UpdateIssueReportCommand(id, resource.status(), userId);
    }

}
