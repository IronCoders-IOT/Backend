package com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.provider;

import java.util.Objects;

public record UpdateProviderCommand (String taxName, String ruc){

        public UpdateProviderCommand{
            Objects.requireNonNull(taxName, "taxName cannot be null");
            Objects.requireNonNull(ruc, "ruc cannot be null");

        }

}
