package com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.water;

import java.time.LocalDateTime;

public record UpdateWaterResource(String status,
                                  LocalDateTime deliveredAt) {
}
