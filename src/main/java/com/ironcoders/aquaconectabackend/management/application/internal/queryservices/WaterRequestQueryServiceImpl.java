package com.ironcoders.aquaconectabackend.management.application.internal.queryservices;
import com.ironcoders.aquaconectabackend.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.WaterRequestAggregate;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetAllWaterRequestsQuery;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetAllWatterRequestsQuery;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetWaterRequestByIdQuery;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetWaterRequestsByResidentIdQuery;
import com.ironcoders.aquaconectabackend.management.domain.services.WaterRequestQueryService;
import com.ironcoders.aquaconectabackend.management.infrastructure.persistence.jpa.repositories.WaterRequestRepository;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Provider;
import com.ironcoders.aquaconectabackend.subcriptions.infrastructure.persistence.jpa.repositories.provider.ProviderRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WaterRequestQueryServiceImpl implements WaterRequestQueryService {

    private final WaterRequestRepository waterRequestRepository;
    private final ProviderRepository providerRepository;

    public WaterRequestQueryServiceImpl(WaterRequestRepository waterRequestRepository, ProviderRepository providerRepository) {
        this.waterRequestRepository = waterRequestRepository;
        this.providerRepository = providerRepository;
    }

    @Override
    public Optional<WaterRequestAggregate> handle(GetWaterRequestByIdQuery query) {
        return waterRequestRepository.findById(query.waterRequestId());
    }

    @Override
    public List<WaterRequestAggregate> handle(GetAllWaterRequestsQuery query) {
        // 1. Obtener el usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        long userId = userDetails.getId();

        // 2. Verificar que el usuario tenga el rol de proveedor
        boolean isProvider = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_PROVIDER"));
        if (!isProvider) {
            throw new IllegalArgumentException("No eres proveedor");
        }

        // 3. Buscar el provider con el userId
        List<Provider> providerOpt = providerRepository.findByUserId(userId);
        if (providerOpt.isEmpty()) {
            throw new IllegalArgumentException("Proveedor no encontrado.");
        }

        Long providerId = providerOpt.get(0).getId();

        // 4. Filtrar las solicitudes por providerId
        List<WaterRequestAggregate> allRequests = waterRequestRepository.findAll();
        return allRequests.stream()
                .filter(request -> request.getProviderId().equals(providerId))
                .toList();
    }


    @Override
    public List<WaterRequestAggregate> handle(GetWaterRequestsByResidentIdQuery query) {
        return waterRequestRepository.findByResidentId(query.residentId()); }

    @Override
    public List<WaterRequestAggregate>handle(GetAllWatterRequestsQuery query){
        return waterRequestRepository.findAll();
    }
}