package com.ironcoders.aquaconectabackend.management.domain.model.queries;

public record GetRequestsByResidentIdQuery(long residentId) {
    public GetRequestsByResidentIdQuery {
        if (residentId <= 0) {
            throw new IllegalArgumentException("Resident ID must be a positive number.");
        }
    }
}
