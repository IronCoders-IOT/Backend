package com.ironcoders.aquaconectabackend.management.application.internal.commandservices;
import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.RequestAggregate;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.CreateRequestCommand;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.UpdateRequestCommand;
import com.ironcoders.aquaconectabackend.management.domain.services.RequestCommandService;
import com.ironcoders.aquaconectabackend.management.infrastructure.persistence.jpa.repositories.RequestRepository;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class RequestCommandServiceImpl implements RequestCommandService {

    private final RequestRepository requestRepository;

    public RequestCommandServiceImpl(RequestRepository requestRepository) {

        this.requestRepository = requestRepository;
    }

    @Override
    public Optional<RequestAggregate> handle(CreateRequestCommand command) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        var requestAggregate = new RequestAggregate(command);
        requestRepository.save(requestAggregate);
        return Optional.of(requestAggregate);
    }

    @Override
    public Optional<RequestAggregate> handle(UpdateRequestCommand command) {
        return Optional.empty();
    }


        
}