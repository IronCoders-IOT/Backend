package com.ironcoders.aquaconectabackend.management.interfaces.rest.resources;

public record CreateEventResource(
        String eventType,
        String qualityValue,
        String levelValue,
        Long sensorId
) {}