package com.ironcoders.aquaconectabackend.dashboard.interfaces;

import com.ironcoders.aquaconectabackend.dashboard.domain.model.aggregates.DashboardResumenDto;
import com.ironcoders.aquaconectabackend.dashboard.domain.services.DashboardQueryService;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetAllWatterRequestsQuery;
import com.ironcoders.aquaconectabackend.management.domain.services.WaterRequestQueryService;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.water.WaterRequestResource;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.transform.water.WaterRequestResourceFromAggregateAssembler;
import com.ironcoders.aquaconectabackend.profiles.infrastructure.persistence.jpa.repositories.ProfileRepository;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Resident;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.queries.resident.GetAllResidentsQuery;
import com.ironcoders.aquaconectabackend.subcriptions.infrastructure.persistence.jpa.repositories.resident.ResidentQueryService;
import com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.resources.resident.ResidentResource;
import com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.transform.resident.ResidentResourceFromEntityAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final ResidentQueryService residentQueryService;
    private final ProfileRepository profileRepository;
    private final WaterRequestQueryService waterRequestQueryService;

    public AdminController(ResidentQueryService residentQueryService, ProfileRepository profileRepository, WaterRequestQueryService waterRequestQueryService) {
        this.residentQueryService = residentQueryService;
        this.profileRepository = profileRepository;
        this.waterRequestQueryService = waterRequestQueryService;
    }

    @GetMapping("/water-requests")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<WaterRequestResource> getAllWaterRequestsByAdmin() {
        return waterRequestQueryService.handle(new GetAllWatterRequestsQuery())
                .stream()
                .map(WaterRequestResourceFromAggregateAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
    }


    @GetMapping("/residents")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ResidentResource>> getAllResidents() {
        List<Resident> residents = residentQueryService.handle(new GetAllResidentsQuery());

        if (residents.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<ResidentResource> resources = new ArrayList<>();

        for (Resident resident : residents) {
            var profileOptional = profileRepository.findByUserId(resident.getUserId());
            if (profileOptional.isEmpty()) {
                continue; // Podrías loguear esto si es un caso raro
            }
            var resource = ResidentResourceFromEntityAssembler.toResourceFromEntity(
                    resident, profileOptional.get(0)
            );
            resources.add(resource);
        }

        return ResponseEntity.ok(resources);
    }



}
