package com.ironcoders.aquaconectabackend.management.interfaces.rest.transform.sensor;

import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.RequestAggregate;
import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.SensorAggregate;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.request.RequestResource;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.sensor.SensorResource;

public class SensorResourceFromEntityAssembler {
    public static SensorResource toResourceFromEntity(SensorAggregate aggregate) {
        return new SensorResource(
            aggregate.getId(),
                aggregate.getStatus(),
                aggregate.getDescription(),
                aggregate.getResidentId()
        );
    }
}