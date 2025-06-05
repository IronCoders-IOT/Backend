package com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.event;

public record EventResource(
        Long id,String eventType,
        String qualityValue,
        String levelValue,
        Long sensorId
) {
}
