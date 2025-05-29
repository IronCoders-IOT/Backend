package com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.transform.resident;

import com.ironcoders.aquaconectabackend.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Resident;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.resident.CreateResidentCommand;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.resident.UpdateResidentCommand;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.queries.resident.GetResidentByUserIdQuery;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.queries.resident.GetResidentsByProviderIdQuery;
import com.ironcoders.aquaconectabackend.subcriptions.domain.services.resident.ResidentCommandService;
import com.ironcoders.aquaconectabackend.subcriptions.infrastructure.persistence.jpa.repositories.resident.ResidentQueryService;
import com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.resources.provider.UpdateProviderResource;
import com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.resources.resident.CreateResidentResource;
import com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.resources.resident.ResidentResource;
import com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.resources.resident.UpdateResidentResource;
import com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.transform.provider.CreateProviderCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/residents", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Residents", description = "Resident Management Endpoints")
public class ResidentController {

    private final ResidentCommandService residentCommandService;
    private final ResidentQueryService residentQueryService;
    //private final ResidentQueryService residentQueryService;


    public ResidentController(ResidentCommandService residentCommandService, /*, ResidentQueryService residentQueryService */ResidentQueryService residentQueryService) {
        this.residentCommandService = residentCommandService;
       // this.residentQueryService = residentQueryService;
        this.residentQueryService = residentQueryService;
    }

    @PostMapping
    public ResponseEntity<ResidentResource> createResident(@RequestBody CreateResidentResource resource){
        CreateResidentCommand createResidentCommand= CreateResidentCommandFromResourceAssembler.toCommandFromResource(resource);
        var resident = residentCommandService.handle(createResidentCommand);
        if (resident.isEmpty())return ResponseEntity.badRequest().build();
        var residentResource = ResidentResourceFromEntityAssembler.toResourceFromEntity(resident.get());
        return new ResponseEntity<>(residentResource, HttpStatus.CREATED);
    }
    @GetMapping("/by-provider/{providerId}")
    public ResponseEntity<List<ResidentResource>> getResidentsByProviderId(@PathVariable Long providerId) {
        var query = new GetResidentsByProviderIdQuery(providerId);
        var residents = residentQueryService.handle(query);
        if (residents.isEmpty()) return ResponseEntity.notFound().build();
        var residentResources = residents.stream().map(ResidentResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(residentResources);

    }

    @GetMapping("/{id}")
    public ResponseEntity<List<ResidentResource>> getResidentsByUserId(@RequestParam Long userId) {
        var query = new GetResidentByUserIdQuery(userId);
        var residents = residentQueryService.handle(query);
        if (residents.isEmpty()) return ResponseEntity.notFound().build();
        var residentResources = residents.stream().map(ResidentResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(residentResources);

    }


    @PutMapping("/me/edit")
    public ResponseEntity<ResidentResource> updateResident(@RequestBody UpdateResidentResource resource) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        long userId = userDetails.getId();

        // Convertir recurso a comando con el ID del path
        UpdateResidentCommand updateResidentCommand = UpdateResidentCommandFromResource.toCommandFromResource(resource);

        Optional<Resident> updatedResidentOptional = residentCommandService.handle(updateResidentCommand);

        return updatedResidentOptional
                .filter(updatedResident -> updatedResident.getUserId() == userId) // validación opcional
                .map(updatedResident -> ResponseEntity.ok(ResidentResourceFromEntityAssembler.toResourceFromEntity(updatedResident)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }




}
