package com.ironcoders.aquaconectabackend.monitoring.interfaces.rest.resources;

import java.time.LocalDateTime;

public record UpdateWaterResource(String status,
                                  LocalDateTime deliveredAt) {
}
