package com.ironcoders.aquaconectabackend.management.domain.model.commads;

public record CreateRequestCommand(
    String title,
    String description,
    String status
) {

}
