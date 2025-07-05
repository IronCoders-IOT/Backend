package com.ironcoders.aquaconectabackend.management.interfaces.rest.acl;

import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.WaterSupplyRequest;
import com.ironcoders.aquaconectabackend.management.domain.services.WaterSupplyRequestQueryService;
import com.ironcoders.aquaconectabackend.profiles.domain.model.queries.GetWaterRequestsByResidentIdQuery;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WaterSupplyRequestContextFacade {

    private final WaterSupplyRequestQueryService waterRequestQueryService;

    public WaterSupplyRequestContextFacade(WaterSupplyRequestQueryService waterRequestQueryService) {
        this.waterRequestQueryService = waterRequestQueryService;
    }

    /**
     * Fetches all water supply requests by residentId.
     *
     * @param residentId the resident id
     * @return a list of WaterSupplyRequest
     */
    public List<WaterSupplyRequest> fetchWaterRequestsByResidentId(Long residentId) {
        return waterRequestQueryService.handle(new GetWaterRequestsByResidentIdQuery(residentId));
    }
}