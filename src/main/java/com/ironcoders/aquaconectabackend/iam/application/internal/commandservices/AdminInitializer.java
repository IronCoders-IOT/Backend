package com.ironcoders.aquaconectabackend.iam.application.internal.commandservices;

import com.ironcoders.aquaconectabackend.iam.domain.model.commands.SignUpCommand;
import com.ironcoders.aquaconectabackend.iam.domain.model.valueobjects.Roles;
import com.ironcoders.aquaconectabackend.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdminInitializer {

    private final SignUpHandler signUpHandler;
    private final UserRepository userRepository;

    public AdminInitializer(SignUpHandler signUpHandler, UserRepository userRepository) {
        this.signUpHandler = signUpHandler;
        this.userRepository = userRepository;
    }

    @PostConstruct
    @Transactional
    public void initAdminUser() {
        if (userRepository.existsByUsername("admin")) {
            System.out.println("ℹ️ Usuario admin ya existe.");
            return;
        }

        signUpHandler.handle(new SignUpCommand("admin", "admin123", List.of()));
        System.out.println("✅ Usuario admin creado con éxito.");
    }
}
