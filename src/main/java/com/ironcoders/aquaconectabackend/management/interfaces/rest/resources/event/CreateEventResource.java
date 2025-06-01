package com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.event;

public record CreateEventResource( String eventType,
        String qualityValue,
        String levelValue,
        Long sensorId
) {}