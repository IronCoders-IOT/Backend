package com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.transform.resident;

import com.ironcoders.aquaconectabackend.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.ironcoders.aquaconectabackend.iam.interfaces.acl.IamContextFacade;
import com.ironcoders.aquaconectabackend.profiles.domain.model.aggregates.Profile;
import com.ironcoders.aquaconectabackend.profiles.infrastructure.persistence.jpa.repositories.ProfileRepository;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Provider;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Resident;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.ResidentWithCredentials;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.resident.CreateResidentCommand;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.resident.UpdateResidentCommand;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.queries.resident.GetResidentByUserIdQuery;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.queries.resident.GetResidentsByProviderIdQuery;
import com.ironcoders.aquaconectabackend.subcriptions.domain.services.resident.ResidentCommandService;
import com.ironcoders.aquaconectabackend.subcriptions.infrastructure.persistence.jpa.repositories.provider.ProviderQueryService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/residents", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Residents", description = "Resident Management Endpoints")
@PreAuthorize("isAuthenticated()")
public class ResidentController {

    private final ResidentCommandService residentCommandService;
    private final ResidentQueryService residentQueryService;
    IamContextFacade iamContextFacade;
    private final ProviderQueryService providerQueryService;
    private final ProfileRepository profileRepository;

    public ResidentController(ResidentCommandService residentCommandService, /*, ResidentQueryService residentQueryService */ResidentQueryService residentQueryService, IamContextFacade iamContextFacade, ProviderQueryService providerQueryService, ProfileRepository profileRepository) {
        this.residentCommandService = residentCommandService;
       // this.residentQueryService = residentQueryService;
        this.residentQueryService = residentQueryService;
        this.iamContextFacade = iamContextFacade;

        this.providerQueryService = providerQueryService;
        this.profileRepository = profileRepository;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_PROVIDER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResidentResource> createResident(@RequestBody CreateResidentResource resource) throws AccessDeniedException {
        // Convertimos el recurso a comando
        CreateResidentCommand command = CreateResidentCommandFromResourceAssembler.toCommandFromResource(resource);

        // Ejecutamos el caso de uso
        ResidentWithCredentials result = residentCommandService.handle(command);

        List<Profile> profiles = profileRepository.findByUserId(result.resident().getUserId());

        // Convertimos a recurso incluyendo username y password generados
        ResidentResource residentResource = ResidentResourceFromEntityAssembler.toResourceFromEntityWithCredentials(
                result.resident(),
                result.username(),
                result.password(),
                profiles.get(0)
        );

        return new ResponseEntity<>(residentResource, HttpStatus.CREATED);
    }


    @GetMapping("/by-provider/{providerId}")
    @PreAuthorize("hasRole('ROLE_PROVIDER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ResidentResource>> getResidentsByProviderId(@PathVariable Long providerId) {
        var query = new GetResidentsByProviderIdQuery(providerId);
        var residents = residentQueryService.handle(query);
        if (residents.isEmpty()) return ResponseEntity.notFound().build();

        var residentResources = residents.stream().map(resident -> {


            List<Profile> profiles = profileRepository.findByUserId(resident.getUserId());
            String username = iamContextFacade.fetchUsernameByUserId(resident.getUserId());
            return ResidentResourceFromEntityAssembler.toResourceFromEntityWithCredentials(resident, username,null, profiles.get(0));
        }).toList();

        return ResponseEntity.ok(residentResources);
    }



    @GetMapping
    @PreAuthorize("hasRole('ROLE_PROVIDER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ResidentResource>> getResidentsForAuthenticatedProvider() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        long userId = userDetails.getId();

        // Buscar el proveedor por su userId
        Optional<Provider> providerOptional = providerQueryService.findByUserId(userId);
        if (providerOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // o 404 si prefieres
        }

        Long providerId = providerOptional.get().getId();

        // Consultar residentes
        var query = new GetResidentsByProviderIdQuery(providerId);
        var residents = residentQueryService.handle(query);
        if (residents.isEmpty()) return ResponseEntity.notFound().build();

        var residentResources = residents.stream().map(resident -> {

            List<Profile> profiles = profileRepository.findByUserId(resident.getUserId());
            String username = iamContextFacade.fetchUsernameByUserId(resident.getUserId());
            return ResidentResourceFromEntityAssembler.toResourceFromEntityWithCredentials(resident, username, null,profiles.get(0));
        }).toList();

        return ResponseEntity.ok(residentResources);
    }


    @GetMapping("/me")
    @PreAuthorize("hasRole('ROLE_RESIDENT')")
    public ResponseEntity<ResidentResource> getAuthenticatedResident() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        long userId = userDetails.getId();

        // Buscar residente por su userId
        List<Resident> residentOptional = residentQueryService.findByUserId (userId);
        if (residentOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Resident resident = residentOptional.get(0);
        String username = iamContextFacade.fetchUsernameByUserId(userId);
        List<Profile> profiles = profileRepository.findByUserId(resident.getUserId());

        ResidentResource resource = ResidentResourceFromEntityAssembler
                .toResourceFromEntityWithCredentials(resident, username, null,profiles.get(0));

        return ResponseEntity.ok(resource);
    }













    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_PROVIDER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ResidentResource>> getResidentsByUserId(@RequestParam Long userId) {
        var query = new GetResidentByUserIdQuery(userId);
        var residents = residentQueryService.handle(query);
        if (residents.isEmpty()) return ResponseEntity.notFound().build();

        // Obtener username para cada userId del residente
        var residentResources = residents.stream().map(resident -> {
            String username = iamContextFacade.fetchUsernameByUserId(resident.getUserId());
            List<Profile> profiles = profileRepository.findByUserId(resident.getUserId());
            return ResidentResourceFromEntityAssembler.toResourceFromEntityWithCredentials(resident, username, null,profiles.get(0));
        }).toList();

        return ResponseEntity.ok(residentResources);
    }



    @PutMapping("/me/edit")
    @PreAuthorize("hasRole('ROLE_RESIDENT')")
    public ResponseEntity<ResidentResource> updateResident(@RequestBody UpdateResidentResource resource) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        long userId = userDetails.getId();

        // Convertir recurso a comando con el ID del path
        UpdateResidentCommand updateResidentCommand = UpdateResidentCommandFromResource.toCommandFromResource(resource);

        Optional<Resident> updatedResidentOptional = residentCommandService.handle(updateResidentCommand);
        List<Profile> profiles = profileRepository.findByUserId(updatedResidentOptional.get().getUserId());

        return updatedResidentOptional
                .filter(updatedResident -> updatedResident.getUserId() == userId) // validación opcional

                .map(updatedResident -> ResponseEntity.ok(ResidentResourceFromEntityAssembler.toResourceFromEntity(updatedResident,profiles.get(0))))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }




}
