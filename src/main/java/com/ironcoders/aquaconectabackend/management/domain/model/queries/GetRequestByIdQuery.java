package com.ironcoders.aquaconectabackend.management.domain.model.queries;

public record GetRequestByIdQuery(Long requestId) {
    public GetRequestByIdQuery {
        if (requestId == null || requestId <= 0) {
            throw new IllegalArgumentException("Request ID must be a positive number.");
        }
    }
}
