package com.ironcoders.aquaconectabackend.management.application.internal.commandservices;

import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.RequestAggregate;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.CreateRequestCommand;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.DeleteRequestCommand;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.UpdateRequestCommand;
import com.ironcoders.aquaconectabackend.management.domain.services.RequestCommandService;
import com.ironcoders.aquaconectabackend.management.infrastructure.persistence.jpa.repositories.RequestRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class RequestCommandServiceImpl implements RequestCommandService {

    private final RequestRepository requestRepository;

    public RequestCommandServiceImpl(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @Override
    public void handle(CreateRequestCommand command) {
        RequestAggregate request = new RequestAggregate(
                command.getResidentId(),
                command.getProviderId(),
                command.getTitle(),
                command.getDescription(),
                command.getStatus()
        );
        requestRepository.save(request);
    }

    @Override
    public void handle(UpdateRequestCommand command) {
        RequestAggregate request = requestRepository.findById(command.getRequestId())
                .orElseThrow(() -> new RuntimeException("Request not found"));
        request.update(command.getTitle(), command.getDescription(), command.getStatus());
        requestRepository.save(request);
    }

    @Override
    public void handle(DeleteRequestCommand command) {
        requestRepository.deleteById(command.getRequestId());
    }
}