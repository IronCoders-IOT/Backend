package com.ironcoders.aquaconectabackend.dashboard.domain.services;

import com.ironcoders.aquaconectabackend.dashboard.domain.model.aggregates.DashboardResumenDto;

public interface DashboardQueryService {
    DashboardResumenDto getDashboard();
}
