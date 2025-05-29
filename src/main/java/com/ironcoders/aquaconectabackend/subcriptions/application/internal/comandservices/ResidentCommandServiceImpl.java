package com.ironcoders.aquaconectabackend.subcriptions.application.internal.comandservices;

import com.ironcoders.aquaconectabackend.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.ironcoders.aquaconectabackend.profiles.infrastructure.persistence.jpa.repositories.ProfileRepository;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Provider;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Resident;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.resident.CreateResidentCommand;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.resident.UpdateResidentCommand;
import com.ironcoders.aquaconectabackend.subcriptions.domain.services.resident.ResidentCommandService;
import com.ironcoders.aquaconectabackend.subcriptions.infrastructure.persistence.jpa.repositories.resident.ResidentRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResidentCommandServiceImpl implements ResidentCommandService {

    private final ResidentRepository residentRepository;
    private final ProfileRepository profileRepository;
    public ResidentCommandServiceImpl(ResidentRepository residentRepository, ProfileRepository profileRepository) {
        this.residentRepository = residentRepository;
        this.profileRepository = profileRepository;
    }

    @Override
    public Optional<Resident> handle(CreateResidentCommand command) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        if (!profileRepository.findById(userDetails.getId()).isPresent()) {
            throw new IllegalArgumentException("No se encontró un perfil para este usuario");
        }



        var resident = new Resident(command, userDetails.getId());
        residentRepository.save(resident);
        return Optional.of(resident);
    }

    @Override
    public Optional<Resident> handle(UpdateResidentCommand command) {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<Resident> existingResident = residentRepository.findByUserId(userDetails.getId());
        if (existingResident.isEmpty()) {
            throw new IllegalArgumentException("No resident found for this user");
        }
        Resident resident = existingResident.get(0);
        resident.update(command);
        residentRepository.save(resident);

        return Optional.of(resident);

    }
}
