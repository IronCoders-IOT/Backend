package com.ironcoders.aquaconectabackend.management.application.internal.queryservices;
import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.RequestAggregate;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetAllRequestQuery;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetAllRequestsByProviderIdQuery;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetRequestByIdQuery;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetAllRequestsByResidentIdQuery;
import com.ironcoders.aquaconectabackend.management.domain.services.RequestQueryService;
import com.ironcoders.aquaconectabackend.management.infrastructure.persistence.jpa.repositories.RequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RequestQueryServiceImpl implements RequestQueryService {

    private final RequestRepository requestRepository;

    public RequestQueryServiceImpl(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @Override
    public Optional<RequestAggregate> handle(GetRequestByIdQuery query) {
        return requestRepository.findById(query.requestId());
    }

    @Override
    public List<RequestAggregate> handle(GetAllRequestsByProviderIdQuery query) {
        return requestRepository.findByProviderId(query.providerId());
    }

    @Override
    public List<RequestAggregate> handle(GetAllRequestQuery query) {
        return requestRepository.findAll();
    }

    @Override
    public List<RequestAggregate> handle(GetAllRequestsByResidentIdQuery query) {
        return requestRepository.findByResidentId(query.residentId());
    }
}
