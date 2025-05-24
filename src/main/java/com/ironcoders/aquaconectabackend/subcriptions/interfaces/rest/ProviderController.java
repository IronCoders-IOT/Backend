package com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest;


import com.ironcoders.aquaconectabackend.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Provider;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.CreateProviderCommand;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.UpdateProviderCommand;
import com.ironcoders.aquaconectabackend.subcriptions.domain.services.ProviderCommandService;
import com.ironcoders.aquaconectabackend.subcriptions.infrastructure.persistence.jpa.repositories.ProviderQueryService;
import com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.resources.CreateProviderResource;
import com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.resources.ProviderResource;
import com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.resources.UpdateProviderResource;
import com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.transform.CreateProviderCommandFromResourceAssembler;
import com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.transform.ProviderResourceFromEntityAssembler;
import com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.transform.UpdateProviderCommandFromResource;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/providers", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Providers", description = "Provider Management Endpoints")
public class ProviderController {
    private final ProviderCommandService providerCommandService;
    private final ProviderQueryService providerQueryService;

    public ProviderController(ProviderCommandService providerCommandService, ProviderQueryService providerQueryService) {
        this.providerCommandService = providerCommandService;
        this.providerQueryService = providerQueryService;
    }

    @PostMapping
    public ResponseEntity<ProviderResource> createProfile(@RequestBody CreateProviderResource resource) {
        CreateProviderCommand createProviderCommand = CreateProviderCommandFromResourceAssembler.toCommandFromResource(resource);
        var provider = providerCommandService.handle(createProviderCommand);
        if (provider.isEmpty()) return ResponseEntity.badRequest().build();
        var providerResource = ProviderResourceFromEntityAssembler.toResourceFromEntity(provider.get());
        return new ResponseEntity<>(providerResource, HttpStatus.CREATED);
    }

    @PutMapping("/edit")
    public ResponseEntity<ProviderResource> updateProvider(@RequestBody UpdateProviderResource resource) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        long userId = userDetails.getId();

        UpdateProviderCommand updateProfileCommand = UpdateProviderCommandFromResource.toCommandFromResource(resource);
        Optional<Provider> updatedProfileOptional = providerCommandService.handle(updateProfileCommand);

        return updatedProfileOptional
                .filter(updatedProfile -> updatedProfile.getUserId() == userId)
                .map(updatedProfile -> ResponseEntity.ok(ProviderResourceFromEntityAssembler.toResourceFromEntity(updatedProfile)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /*
    @GetMapping("/me")
    public ResponseEntity<ProfileResource> getMyProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        var getProfileByIdQuery = new GetProfileByIdQuery(userDetails.getId());
        var profile = profileQueryService.handle(getProfileByIdQuery);
        if (profile.isEmpty()) return ResponseEntity.notFound().build();
        var profileResource = ProfileResourceFromEntityAssembler.toResourceFromEntity(profile.get());
        return ResponseEntity.ok(profileResource);
    }


     */

}
