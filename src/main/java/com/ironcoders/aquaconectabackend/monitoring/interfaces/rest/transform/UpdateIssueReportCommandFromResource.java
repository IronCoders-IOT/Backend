package com.ironcoders.aquaconectabackend.monitoring.interfaces.rest.transform;

import com.ironcoders.aquaconectabackend.monitoring.domain.model.commads.UpdateIssueReportCommand;
import com.ironcoders.aquaconectabackend.monitoring.interfaces.rest.resources.UpdateIssueReportResource;

public class UpdateIssueReportCommandFromResource {

    public static UpdateIssueReportCommand toCommandFromResource(Long id, UpdateIssueReportResource resource, Long userId) {
        return new UpdateIssueReportCommand(id, resource.status(), userId);
    }

}
