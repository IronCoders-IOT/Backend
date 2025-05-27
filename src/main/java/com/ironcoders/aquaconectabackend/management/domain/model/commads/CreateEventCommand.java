package com.ironcoders.aquaconectabackend.management.domain.model.commads;

public record CreateEventCommand(
        String eventType,
        String qualityValue,
        String levelValue,
        Long sensorId
) {}