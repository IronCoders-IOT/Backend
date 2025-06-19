package com.ironcoders.aquaconectabackend;

import com.ironcoders.aquaconectabackend.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.ironcoders.aquaconectabackend.profiles.domain.model.aggregates.Profile;
import com.ironcoders.aquaconectabackend.profiles.infrastructure.persistence.jpa.repositories.ProfileRepository;
import com.ironcoders.aquaconectabackend.subcriptions.application.internal.comandservices.ProviderCommandServiceImpl;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Provider;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.provider.CreateProviderCommand;
import com.ironcoders.aquaconectabackend.subcriptions.infrastructure.persistence.jpa.repositories.provider.ProviderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProviderCommandServiceImplTest {

    @Mock
    private ProviderRepository providerRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetailsImpl userDetails;

    @InjectMocks
    private ProviderCommandServiceImpl providerCommandService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Simular contexto de seguridad
        SecurityContextHolder.clearContext();
        when(authentication.getPrincipal()).thenReturn(userDetails);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void handleCreateProvider_whenProfileNotExists_createsProfileAndProvider() {
        // Arrange
        Long userId = 1L;
        when(userDetails.getId()).thenReturn(userId);
        when(profileRepository.findByUserId(userId)).thenReturn(List.of(mock(Profile.class)));

        CreateProviderCommand command = mock(CreateProviderCommand.class);
        when(command.firstName()).thenReturn("John");
        when(command.lastName()).thenReturn("Doe");
        when(command.email()).thenReturn("john@doe.com");
        when(command.direction()).thenReturn("Street 123");
        when(command.documentNumber()).thenReturn("12345678");
        when(command.documentType()).thenReturn("DNI");
        when(command.phone()).thenReturn("555-1234");

        // Act
        Optional<Provider> result = providerCommandService.handle(command);

        // Assert
        assertTrue(result.isPresent());
        verify(profileRepository, times(1)).save(any(Profile.class));
        verify(providerRepository, times(1)).save(any(Provider.class));
    }

}