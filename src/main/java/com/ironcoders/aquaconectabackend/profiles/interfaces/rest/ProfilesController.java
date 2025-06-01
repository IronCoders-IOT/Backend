package com.ironcoders.aquaconectabackend.profiles.interfaces.rest;


import com.ironcoders.aquaconectabackend.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.ironcoders.aquaconectabackend.profiles.domain.model.aggregates.Profile;
import com.ironcoders.aquaconectabackend.profiles.domain.model.commands.CreateProfileCommand;
import com.ironcoders.aquaconectabackend.profiles.domain.model.commands.UpdateProfileCommand;
import com.ironcoders.aquaconectabackend.profiles.domain.model.queries.GetProfileByIdQuery;
import com.ironcoders.aquaconectabackend.profiles.domain.model.queries.GetProfileByUserIdQuery;
import com.ironcoders.aquaconectabackend.profiles.domain.services.ProfileCommandService;
import com.ironcoders.aquaconectabackend.profiles.domain.services.ProfileQueryService;
import com.ironcoders.aquaconectabackend.profiles.interfaces.rest.resources.CreateProfileResource;
import com.ironcoders.aquaconectabackend.profiles.interfaces.rest.resources.ProfileResource;
import com.ironcoders.aquaconectabackend.profiles.interfaces.rest.resources.UpdateProfileResource;
import com.ironcoders.aquaconectabackend.profiles.interfaces.rest.transform.CreateProfileCommandFromResourceAssembler;
import com.ironcoders.aquaconectabackend.profiles.interfaces.rest.transform.ProfileResourceFromEntityAssembler;
import com.ironcoders.aquaconectabackend.profiles.interfaces.rest.transform.UpdateProfileCommandFromResource;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/profiles", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Profiles", description = "Profile Management Endpoints")
public class ProfilesController {
    private final ProfileCommandService profileCommandService;
    private final ProfileQueryService profileQueryService;

    public ProfilesController(ProfileCommandService profileCommandService, ProfileQueryService profileQueryService) {
        this.profileCommandService = profileCommandService;
        this.profileQueryService = profileQueryService;
    }

    @PostMapping
    public ResponseEntity<ProfileResource> createProfile(@RequestBody CreateProfileResource resource) {
        CreateProfileCommand createProfileCommand = CreateProfileCommandFromResourceAssembler.toCommandFromResource(resource);
        var profile = profileCommandService.handle(createProfileCommand);
        if (profile.isEmpty()) return ResponseEntity.badRequest().build();
        var profileResource = ProfileResourceFromEntityAssembler.toResourceFromEntity(profile.get());
        return new ResponseEntity<>(profileResource, HttpStatus.CREATED);
    }

    @GetMapping("/me")
    public ResponseEntity<ProfileResource> getMyProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        var getProfileByIdQuery = new GetProfileByUserIdQuery(userDetails.getId());
        var profile = profileQueryService.handle(getProfileByIdQuery);
        if (profile.isEmpty()) return ResponseEntity.notFound().build();
        var profileResource = ProfileResourceFromEntityAssembler.toResourceFromEntity(profile.get());
        return ResponseEntity.ok(profileResource);
    }
    @PutMapping("/me/edit")
    public ResponseEntity<ProfileResource> updateProfile(@RequestBody UpdateProfileResource resource) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        long userId = userDetails.getId();

        UpdateProfileCommand updateProfileCommand = UpdateProfileCommandFromResource.toCommandFromResource(resource);
        Optional<Profile> updatedProfileOptional = profileCommandService.handle(updateProfileCommand);

        return updatedProfileOptional
                .filter(updatedProfile -> updatedProfile.getUserId() == userId)
                .map(updatedProfile -> ResponseEntity.ok(ProfileResourceFromEntityAssembler.toResourceFromEntity(updatedProfile)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
