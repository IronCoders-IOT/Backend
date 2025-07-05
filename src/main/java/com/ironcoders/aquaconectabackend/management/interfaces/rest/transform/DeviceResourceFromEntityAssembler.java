package com.ironcoders.aquaconectabackend.management.interfaces.rest.transform;

import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.IssueReport;
import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.Device;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.DeviceResource;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.IssueReportResource;

public class DeviceResourceFromEntityAssembler {
    public static DeviceResource toResourceFromEntity(Device aggregate) {
        return new DeviceResource(
            aggregate.getId(),
                aggregate.getStatus(),
                aggregate.getDescription(),
                aggregate.getResidentId()
        );
    }
}