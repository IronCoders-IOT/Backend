package com.ironcoders.aquaconectabackend.management.domain.model.commads;

public record CreateDeviceCommand(
        String type,
        String status,
        String description,
        Long residentId
) {
   
}