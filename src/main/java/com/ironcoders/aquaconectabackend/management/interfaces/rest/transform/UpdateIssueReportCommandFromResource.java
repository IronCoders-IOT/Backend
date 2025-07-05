package com.ironcoders.aquaconectabackend.management.interfaces.rest.transform;

import com.ironcoders.aquaconectabackend.management.domain.model.commads.UpdateIssueReportCommand;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.UpdateIssueReportResource;

public class UpdateIssueReportCommandFromResource {

    public static UpdateIssueReportCommand toCommandFromResource(Long id, UpdateIssueReportResource resource, Long userId) {
        return new UpdateIssueReportCommand(id, resource.status(), userId);
    }

}
