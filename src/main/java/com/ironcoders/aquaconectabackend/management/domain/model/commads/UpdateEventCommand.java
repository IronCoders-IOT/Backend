package com.ironcoders.aquaconectabackend.management.domain.model.commads;

public record UpdateEventCommand(
        Long eventId,
        String eventType,
        String qualityValue,
        String levelValue,
        Long sensorId
) {}