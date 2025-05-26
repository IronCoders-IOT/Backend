package com.ironcoders.aquaconectabackend.management.domain.services;

import com.ironcoders.aquaconectabackend.management.domain.model.commads.CreateRequestCommand;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.DeleteRequestCommand;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.UpdateRequestCommand;

public interface RequestCommandService {
    void handle(CreateRequestCommand command);
    void handle(UpdateRequestCommand command);
    void handle(DeleteRequestCommand command);
}
