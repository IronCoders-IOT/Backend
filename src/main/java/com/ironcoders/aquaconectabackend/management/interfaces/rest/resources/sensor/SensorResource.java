package com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.sensor;

public record SensorResource(
        Long id,
        String type,
        String status,
        Long residentId
){
}
