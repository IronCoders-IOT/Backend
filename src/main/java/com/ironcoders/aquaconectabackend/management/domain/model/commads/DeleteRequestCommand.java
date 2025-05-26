package com.ironcoders.aquaconectabackend.management.domain.model.commads;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DeleteRequestCommand {
    @NotNull
    private final Long requestId;
}
