package com.ironcoders.aquaconectabackend.iam.application.internal.commandservices;

import com.ironcoders.aquaconectabackend.iam.application.internal.outboundservices.hashing.HashingService;
import com.ironcoders.aquaconectabackend.iam.domain.model.aggregates.User;
import com.ironcoders.aquaconectabackend.iam.domain.model.commands.SignUpCommand;
import com.ironcoders.aquaconectabackend.iam.domain.model.entities.Role;
import com.ironcoders.aquaconectabackend.iam.domain.model.valueobjects.Roles;
import com.ironcoders.aquaconectabackend.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import com.ironcoders.aquaconectabackend.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component

public class SignUpHandler {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final HashingService hashingService;

    public SignUpHandler(UserRepository userRepository, RoleRepository roleRepository, HashingService hashingService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.hashingService = hashingService;
    }

    @Transactional
    public Optional<User> handle(SignUpCommand command) {
        if (userRepository.existsByUsername(command.username()))
            return Optional.empty();

        List<Role> roles;


        // Si el rol recibido contiene "ROLE_RESIDENT", úsalo
        if (command.roles() != null && command.roles().contains("ROLE_RESIDENT")) {
            Role residentRole = roleRepository.findByName(Roles.ROLE_RESIDENT)
                    .orElseThrow(() -> new RuntimeException("Rol ROLE_RESIDENT no encontrado"));
            roles = List.of(residentRole);
        } else {
            // Por defecto: ROLE_PROVIDER
            Role providerRole = roleRepository.findByName(Roles.ROLE_PROVIDER)
                    .orElseThrow(() -> new RuntimeException("Rol ROLE_PROVIDER no encontrado"));
            roles = List.of(providerRole);
        }



        var user = new User(command.username(), hashingService.encode(command.password()), roles);
        userRepository.save(user);
        return userRepository.findByUsername(command.username());
    }
}