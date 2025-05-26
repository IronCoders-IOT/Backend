package com.ironcoders.aquaconectabackend.management.domain.model.commads;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateRequestCommand {

    @NotNull
    private final Long residentId;
    @NotNull
    private final Long providerId;

    @NotBlank
    @Size(max = 255)
    private final String title;

    @NotBlank
    @Size(max = 255)
    private final String description;

    @NotBlank
    @Size(max = 50)
    private final String status;

}
