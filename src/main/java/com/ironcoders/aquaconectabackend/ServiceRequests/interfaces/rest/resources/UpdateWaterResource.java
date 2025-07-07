package com.ironcoders.aquaconectabackend.ServiceRequests.interfaces.rest.resources;

import java.time.LocalDateTime;

public record UpdateWaterResource(String status,
                                  LocalDateTime deliveredAt) {
}
