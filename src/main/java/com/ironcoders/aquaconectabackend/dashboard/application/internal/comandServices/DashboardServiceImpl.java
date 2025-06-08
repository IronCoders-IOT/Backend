package com.ironcoders.aquaconectabackend.dashboard.application.internal.comandServices;

import com.ironcoders.aquaconectabackend.dashboard.domain.model.aggregates.DashboardResumenDto;
import com.ironcoders.aquaconectabackend.dashboard.domain.services.DashboardQueryService;
import com.ironcoders.aquaconectabackend.subcriptions.infrastructure.persistence.jpa.repositories.provider.ProviderRepository;
import com.ironcoders.aquaconectabackend.subcriptions.infrastructure.persistence.jpa.repositories.resident.ResidentRepository;
import com.ironcoders.aquaconectabackend.subcriptions.infrastructure.persistence.jpa.repositories.subscription.SubscriptionRepository;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl implements DashboardQueryService {
    private final ProviderRepository providerRepository;
    private final ResidentRepository residentRepository;
    private final SubscriptionRepository subscriptionRepository;

    public DashboardServiceImpl(ProviderRepository providerRepository, ResidentRepository residentRepository, SubscriptionRepository subscriptionRepository) {
        this.providerRepository = providerRepository;
        this.residentRepository = residentRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public DashboardResumenDto getDashboard() {

        long totalProveedores = providerRepository.count();
        long totalResidentes = residentRepository.count();
        long suscripcionesActivas = subscriptionRepository.countByStatus("ACTIVE");

        // Suma de ingresos
        Float ingresosTotales = subscriptionRepository.sumAllPrices();
        Float ingresosMensual = subscriptionRepository.sumCurrentMonthPrices();

        return new DashboardResumenDto(
                totalProveedores,
                totalResidentes,
                suscripcionesActivas,
                ingresosTotales != null ? ingresosTotales : 0f,
                ingresosMensual != null ? ingresosMensual : 0f
        );
    }
}
