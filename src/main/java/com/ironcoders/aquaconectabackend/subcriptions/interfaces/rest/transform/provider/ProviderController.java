package com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.transform.provider;


import com.ironcoders.aquaconectabackend.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.ironcoders.aquaconectabackend.profiles.domain.model.aggregates.Profile;
import com.ironcoders.aquaconectabackend.profiles.infrastructure.persistence.jpa.repositories.ProfileRepository;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Provider;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.provider.CreateProviderCommand;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.provider.UpdateProviderCommand;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.queries.provider.GetProviderByUserIdQuery;
import com.ironcoders.aquaconectabackend.subcriptions.domain.services.provider.ProviderCommandService;
import com.ironcoders.aquaconectabackend.subcriptions.infrastructure.persistence.jpa.repositories.provider.ProviderQueryService;
import com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.resources.provider.CreateProviderResource;
import com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.resources.provider.ProviderResource;
import com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.resources.provider.UpdateProviderResource;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/providers", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Providers", description = "Provider Management Endpoints")
@PreAuthorize("isAuthenticated()")
public class ProviderController {
    private final ProviderCommandService providerCommandService;
    private final ProviderQueryService providerQueryService;
    private final ProfileRepository profileRepository;

    public ProviderController(ProviderCommandService providerCommandService, ProviderQueryService providerQueryService, ProfileRepository profileRepository) {
        this.providerCommandService = providerCommandService;
        this.providerQueryService = providerQueryService;
        this.profileRepository = profileRepository;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_PROVIDER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ProviderResource> createProfile(@RequestBody CreateProviderResource resource) {
        CreateProviderCommand createProviderCommand = CreateProviderCommandFromResourceAssembler.toCommandFromResource(resource);

        var providerOptional = providerCommandService.handle(createProviderCommand);
        if (providerOptional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var provider = providerOptional.get();

        // Obtener el perfil recién creado
        var profileOptional = profileRepository.findById(provider.getUserId());
        if (profileOptional.isEmpty()) {
            return ResponseEntity.internalServerError().build(); // No debería pasar si todo funcionó bien
        }

        var providerResource = ProviderResourceFromEntityAssembler.toResourceFromEntities(provider, profileOptional.get());
        return new ResponseEntity<>(providerResource, HttpStatus.CREATED);
    }


    @PutMapping("/edit")
    @PreAuthorize("hasRole('ROLE_PROVIDER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ProviderResource> updateProvider(@RequestBody UpdateProviderResource resource) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        long userId = userDetails.getId();

        UpdateProviderCommand updateProviderCommand = UpdateProviderCommandFromResource.toCommandFromResource(resource);
        Optional<Provider> updatedProviderOptional = providerCommandService.handle(updateProviderCommand);

        if (updatedProviderOptional.isEmpty() || updatedProviderOptional.get().getUserId() != userId) {
            return ResponseEntity.notFound().build();
        }

        Provider updatedProvider = updatedProviderOptional.get();

        // Obtener el perfil actualizado
        Optional<Profile> profileOptional = profileRepository.findById(userId);
        if (profileOptional.isEmpty()) {
            return ResponseEntity.internalServerError().build(); // No debería pasar
        }

        ProviderResource response = ProviderResourceFromEntityAssembler.toResourceFromEntities(updatedProvider, profileOptional.get());
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{providerId}/detail")
    @PreAuthorize("hasRole('ROLE_PROVIDER') or hasRole('ROLE_RESIDENT')")
    public ResponseEntity<ProviderResource> getProviderById(@PathVariable Long providerId) {
        // Obtener el proveedor
        var query = new GetProviderByUserIdQuery(providerId);
        var providerOptional = providerQueryService.handle(query);

        if (providerOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var provider = providerOptional.get();

        // Obtener el perfil asociado al usuario del proveedor
        var profileOptional = profileRepository.findById(provider.getUserId());
        if (profileOptional.isEmpty()) {
            return ResponseEntity.internalServerError().build(); // No debería pasar
        }

        var resource = ProviderResourceFromEntityAssembler.toResourceFromEntities(provider, profileOptional.get());
        return ResponseEntity.ok(resource);
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('ROLE_PROVIDER')")
    public ResponseEntity<ProviderResource> getMyProviderDetails() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                System.out.println("DEBUG: No authentication found");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            if (!(authentication.getPrincipal() instanceof UserDetailsImpl)) {
                System.out.println("DEBUG: Principal is not UserDetailsImpl");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            Long userId = userDetails.getId();
            System.out.println("DEBUG: Current userId: " + userId);

            var query = new GetProviderByUserIdQuery(userId);
            var providerOptional = providerQueryService.handle(query);

            if (providerOptional.isEmpty()) {
                System.out.println("DEBUG: No provider found for userId: " + userId);
                return ResponseEntity.notFound().build();
            }

            var provider = providerOptional.get();
            System.out.println("DEBUG: Found provider with ID: " + provider.getId());

            var profileOptional = profileRepository.findByUserId(provider.getUserId()).stream().findFirst();
            if (profileOptional.isEmpty()) {
                System.out.println("DEBUG: No profile found for userId: " + provider.getUserId());
                return ResponseEntity.internalServerError().build();
            }

            var resource = ProviderResourceFromEntityAssembler.toResourceFromEntities(provider, profileOptional.get());
            return ResponseEntity.ok(resource);
        } catch (Exception e) {
            System.out.println("ERROR in getMyProviderDetails: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }



}
