package com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.transform.subscription;


import com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.subscription.CreateSubscriptionCommand;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.queries.subscription.GetAllSubscriptions;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.queries.subscription.GetAllSubscriptionsByResidentId;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.queries.subscription.GetSubscriptionByUserId;
import com.ironcoders.aquaconectabackend.subcriptions.domain.services.subscription.SubscriptionCommandService;
import com.ironcoders.aquaconectabackend.subcriptions.infrastructure.persistence.jpa.repositories.subscription.SubscriptionQueryService;
import com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.resources.subscription.CreateSubscriptionResource;
import com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.resources.subscription.SubscriptionResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/subscriptions", produces = MediaType.APPLICATION_JSON_VALUE )
public class SubscriptionController {

    private final SubscriptionCommandService subscriptionCommandService;
    private final SubscriptionQueryService subscriptionQueryService;
//    private final SubscriptionQueryService subscriptionQueryService;


    public SubscriptionController(SubscriptionCommandService subscriptionCommandService,/*, SubscriptionQueryService subscriptionQueryService*/SubscriptionQueryService subscriptionQueryService) {
        this.subscriptionCommandService = subscriptionCommandService;
//        this.subscriptionQueryService = subscriptionQueryService;
        this.subscriptionQueryService = subscriptionQueryService;
    }

    @PostMapping
    public ResponseEntity<SubscriptionResource> createSubscription(@RequestBody CreateSubscriptionResource resource) {
        CreateSubscriptionCommand createSubscriptionCommand= CreateSubscriptionCommandFromResourceAssembler.toCommandFromResource(resource);
        var subscription= subscriptionCommandService.handle(createSubscriptionCommand);
        if (subscription.isEmpty())return ResponseEntity.badRequest().build();
        var subscriptionResource = SubscriptionResourceFromEntityAssembler.toResourceFromEntity(subscription.get());
        return new ResponseEntity<>(subscriptionResource, HttpStatus.CREATED);
    }

    @GetMapping("/resident/{id}")
    public ResponseEntity<SubscriptionResource> getSubscriptionsByResidentId(@PathVariable Long id){

        var query = new GetAllSubscriptionsByResidentId(id);
        var subscriptions = subscriptionQueryService.handle(query);
        if (subscriptions.isEmpty())return ResponseEntity.notFound().build();
        var subscriptionResource = SubscriptionResourceFromEntityAssembler.toResourceFromEntity(subscriptions.get(0));
        return new ResponseEntity<>(subscriptionResource, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<List<SubscriptionResource>> getSubscriptions() {
        var query = new GetAllSubscriptions();
        var subscriptions = subscriptionQueryService.handle(query);
        if (subscriptions.isEmpty()) return ResponseEntity.noContent().build();

        var resources = subscriptions.stream()
                .map(SubscriptionResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }





}

