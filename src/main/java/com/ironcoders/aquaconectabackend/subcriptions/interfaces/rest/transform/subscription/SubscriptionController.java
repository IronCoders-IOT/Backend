package com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.transform.subscription;


import com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.subscription.CreateSubscriptionCommand;
import com.ironcoders.aquaconectabackend.subcriptions.domain.services.subscription.SubscriptionCommandService;
import com.ironcoders.aquaconectabackend.subcriptions.infrastructure.persistence.jpa.repositories.subscription.SubscriptionQueryService;
import com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.resources.subscription.CreateSubscriptionResource;
import com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.resources.subscription.SubscriptionResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/subscriptions", produces = MediaType.APPLICATION_JSON_VALUE )
public class SubscriptionController {

    private final SubscriptionCommandService subscriptionCommandService;
//    private final SubscriptionQueryService subscriptionQueryService;


    public SubscriptionController(SubscriptionCommandService subscriptionCommandService/*, SubscriptionQueryService subscriptionQueryService*/) {
        this.subscriptionCommandService = subscriptionCommandService;
//        this.subscriptionQueryService = subscriptionQueryService;
    }

    @PostMapping
    public ResponseEntity<SubscriptionResource> createSubscription(@RequestBody CreateSubscriptionResource resource) {
        CreateSubscriptionCommand createSubscriptionCommand= CreateSubscriptionCommandFromResourceAssembler.toCommandFromResource(resource);
        var subscription= subscriptionCommandService.handle(createSubscriptionCommand);
        if (subscription.isEmpty())return ResponseEntity.badRequest().build();
        var subscriptionResource = SubscriptionResourceFromEntityAssembler.toResourceFromEntity(subscription.get());
        return new ResponseEntity<>(subscriptionResource, HttpStatus.CREATED);
    }



}

