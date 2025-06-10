package com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.transform.provider;


import com.ironcoders.aquaconectabackend.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.ironcoders.aquaconectabackend.profiles.domain.model.aggregates.Profile;
import com.ironcoders.aquaconectabackend.profiles.infrastructure.persistence.jpa.repositories.ProfileRepository;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Provider;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.provider.CreateProviderCommand;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.provider.UpdateProviderCommand;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.queries.provider.GetAllProvidersQuery;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.queries.provider.GetProviderByUserIdQuery;
import com.ironcoders.aquaconectabackend.subcriptions.domain.services.provider.ProviderCommandService;
import com.ironcoders.aquaconectabackend.subcriptions.infrastructure.persistence.jpa.repositories.provider.ProviderQueryService;
import com.ironcoders.aquaconectabackend.subcriptions.infrastructure.persistence.jpa.repositories.provider.ProviderRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/providers", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Providers", description = "Provider Management Endpoints")
@PreAuthorize("isAuthenticated()")
public class ProviderController {
    private final ProviderCommandService providerCommandService;
    private final ProviderQueryService providerQueryService;
    private final ProfileRepository profileRepository;
    private final ProviderRepository providerRepository;


    public ProviderController(ProviderCommandService providerCommandService, ProviderQueryService providerQueryService, ProfileRepository profileRepository, ProviderRepository providerRepository) {
        this.providerCommandService = providerCommandService;
        this.providerQueryService = providerQueryService;
        this.profileRepository = profileRepository;
        this.providerRepository = providerRepository;
    }
    @PostMapping
    @PreAuthorize("hasRole('ROLE_PROVIDER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createProfile(@RequestBody CreateProviderResource resource) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        long userId = userDetails.getId();

        // ✅ Validar si ya existe un perfil para el usuario autenticado
        List<Profile> profiles = profileRepository.findByUserId(userId);
        if (!profiles.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Ya existe un perfil asociado a esta cuenta.");
        }

        // Crear proveedor
        CreateProviderCommand createProviderCommand = CreateProviderCommandFromResourceAssembler.toCommandFromResource(resource);
        var providerOptional = providerCommandService.handle(createProviderCommand);

        if (providerOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Error al crear proveedor.");
        }

        var provider = providerOptional.get();

        // Buscar perfil nuevamente después de creación
        List<Profile> updatedProfiles = profileRepository.findByUserId(userId);
        if (updatedProfiles.isEmpty()) {
            return ResponseEntity.internalServerError().body("Perfil no encontrado tras la creación.");
        }

        var providerResource = ProviderResourceFromEntityAssembler.toResourceFromEntities(provider, updatedProfiles.get(0));
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
        List<Profile> profileOptional = profileRepository.findByUserId(userId);
        if (profileOptional.isEmpty()) {
            return ResponseEntity.internalServerError().build(); // No debería pasar
        }

        ProviderResource response = ProviderResourceFromEntityAssembler.toResourceFromEntities(updatedProvider, profileOptional.get(0));
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{providerId}/detail")
    @PreAuthorize("hasRole('ROLE_PROVIDER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ProviderResource> getProviderById(@PathVariable Long providerId) {
        var providerOptional = providerRepository.findById(providerId); // <- directo por ID

        if (providerOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var provider = providerOptional.get();

        var profileOptional = profileRepository.findByUserId(provider.getUserId());
        if (profileOptional.isEmpty()) {
            return ResponseEntity.internalServerError().build();
        }

        var resource = ProviderResourceFromEntityAssembler.toResourceFromEntities(provider, profileOptional.get(0));
        return ResponseEntity.ok(resource);
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('ROLE_PROVIDER')")
    public ResponseEntity<ProviderResource> getMyProviderDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long userId = userDetails.getId();

        // Obtener el proveedor a partir del userId
        var query = new GetProviderByUserIdQuery(userId);
        var providerOptional = providerQueryService.handle(query);

        if (providerOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var provider = providerOptional.get();

        // Obtener el perfil asociado
        var profileOptional = profileRepository.findByUserId(provider.getUserId());
        if (profileOptional.isEmpty()) {
            return ResponseEntity.internalServerError().build(); // No debería ocurrir
        }

        var resource = ProviderResourceFromEntityAssembler.toResourceFromEntities(provider, profileOptional.get(0));
        return ResponseEntity.ok(resource);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ProviderResource>> getAllProviders() {
        List<Provider> providers = providerQueryService.handle(new GetAllProvidersQuery());

        if (providers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<ProviderResource> resources = new ArrayList<>();

        for (Provider provider : providers) {
            var profileOptional = profileRepository.findByUserId(provider.getUserId());
            if (profileOptional.isEmpty()) {
                continue; // Podrías loguear esto si es un caso raro
            }
            var resource = ProviderResourceFromEntityAssembler.toResourceFromEntities(
                    provider, profileOptional.get(0)
            );
            resources.add(resource);
        }

        return ResponseEntity.ok(resources);
    }



}
