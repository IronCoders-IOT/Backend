package com.ironcoders.aquaconectabackend.management.domain.services;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.RequestAggregate;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.CreateRequestCommand;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.UpdateRequestCommand;

public interface RequestCommandService {
  Optional<RequestAggregate> handle(CreateRequestCommand command) throws AccessDeniedException;
  Optional<RequestAggregate> handle(UpdateRequestCommand command) throws AccessDeniedException;
}
