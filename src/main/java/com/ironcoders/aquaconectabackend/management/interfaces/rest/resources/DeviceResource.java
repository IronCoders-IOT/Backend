package com.ironcoders.aquaconectabackend.management.interfaces.rest.resources;

public record DeviceResource(
        Long id,
        String type,
        String status,
        Long residentId
){
}
