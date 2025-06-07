package com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.transform.subscription;

import com.ironcoders.aquaconectabackend.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Resident;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Subscription;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.subscription.CreateSubscriptionCommand;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.subscription.UpdateSubscriptionCommand;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.queries.subscription.GetAllSubscriptions;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.queries.subscription.GetAllSubscriptionsByResidentId;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.queries.subscription.GetSubscriptionByUserId;
import com.ironcoders.aquaconectabackend.subcriptions.domain.services.subscription.SubscriptionCommandService;
import com.ironcoders.aquaconectabackend.subcriptions.infrastructure.persistence.jpa.repositories.resident.ResidentQueryService;
import com.ironcoders.aquaconectabackend.subcriptions.infrastructure.persistence.jpa.repositories.subscription.SubscriptionQueryService;
import com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.resources.subscription.CreateSubscriptionResource;
import com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.resources.subscription.SubscriptionResource;
import com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.resources.subscription.UpdateSubscriptionResource;
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
@RequestMapping(value = "/api/v1/subscriptions", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Subscriptions", description = "Subscription Management Endpoints")
@PreAuthorize("isAuthenticated()")
public class SubscriptionController {

    private final SubscriptionCommandService subscriptionCommandService;
    private final SubscriptionQueryService subscriptionQueryService;
    private final ResidentQueryService residentQueryService;
//    private final SubscriptionQueryService subscriptionQueryService;


    public SubscriptionController(SubscriptionCommandService subscriptionCommandService,/*, SubscriptionQueryService subscriptionQueryService*/SubscriptionQueryService subscriptionQueryService, ResidentQueryService residentQueryService) {
        this.subscriptionCommandService = subscriptionCommandService;
//        this.subscriptionQueryService = subscriptionQueryService;
        this.subscriptionQueryService = subscriptionQueryService;
        this.residentQueryService = residentQueryService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_PROVIDER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<SubscriptionResource> createSubscription(@RequestBody CreateSubscriptionResource resource) {
        CreateSubscriptionCommand createSubscriptionCommand= CreateSubscriptionCommandFromResourceAssembler.toCommandFromResource(resource);
        var subscription= subscriptionCommandService.handle(createSubscriptionCommand);
        if (subscription.isEmpty())return ResponseEntity.badRequest().build();
        var subscriptionResource = SubscriptionResourceFromEntityAssembler.toResourceFromEntity(subscription.get());
        return new ResponseEntity<>(subscriptionResource, HttpStatus.CREATED);
    }

    @GetMapping("/resident/{id}")
    @PreAuthorize("hasRole('ROLE_PROVIDER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_RESIDENT')")
    public ResponseEntity<List<SubscriptionResource>> getSubscriptionsByResidentId(@PathVariable Long id) throws AccessDeniedException {

        var query = new GetAllSubscriptionsByResidentId(id);
        var subscriptions = subscriptionQueryService.handle(query);

        if (subscriptions.isEmpty()) return ResponseEntity.notFound().build();

        var subscriptionResources = subscriptions.stream()
                .map(SubscriptionResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return new ResponseEntity<>(subscriptionResources, HttpStatus.OK);
    }

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_PROVIDER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<SubscriptionResource>> getSubscriptions() throws AccessDeniedException {
        var query = new GetAllSubscriptions();
        var subscriptions = subscriptionQueryService.handle(query);

        if (subscriptions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        var subscriptionResources = subscriptions.stream()
                .map(SubscriptionResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(subscriptionResources);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_PROVIDER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<SubscriptionResource> updateSubscription(
            @PathVariable Long id,
            @RequestBody UpdateSubscriptionResource resource
    ) {
        // Convertir el UpdateSubscriptionResource en UpdateSubscriptionCommand
        UpdateSubscriptionCommand command = UpdateSubscriptionCommandFromResource.toCommand(id, resource);

        Optional<Subscription> updated = subscriptionCommandService.handle(command);
        if (updated.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Subscription agg = updated.get();
        // Convertir la entidad agregada a recurso (DTO) para la respuesta
        SubscriptionResource responseBody = SubscriptionResourceFromEntityAssembler.toResourceFromEntity(agg);
        return ResponseEntity.ok(responseBody);
    }



}

