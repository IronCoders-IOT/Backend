package com.ironcoders.aquaconectabackend.management.interfaces.rest.resources;

public record CreateRequestResource(
        String eventType,
        String qualityValue,
        String levelValue,
        Long sensorId
) {}