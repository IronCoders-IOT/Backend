package com.ironcoders.aquaconectabackend.management.domain.services;

import com.ironcoders.aquaconectabackend.management.domain.model.commads.CreateEventCommand;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.DeleteEventCommand;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.UpdateEventCommand;

public interface EventCommandService {
    void handle(CreateEventCommand command);
    void handle(UpdateEventCommand command);
    void handle(DeleteEventCommand command);
}