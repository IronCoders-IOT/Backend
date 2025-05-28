package com.ironcoders.aquaconectabackend.management.domain.model.commads;

public record UpdateRequestCommand(
    Long requestId,
    Long residentId,
    Long providerId,
    String title,
    String description
) {
}




