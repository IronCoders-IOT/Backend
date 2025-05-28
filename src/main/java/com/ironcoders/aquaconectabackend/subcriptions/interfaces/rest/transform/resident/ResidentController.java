package com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.transform.resident;

import com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.resident.CreateResidentCommand;
import com.ironcoders.aquaconectabackend.subcriptions.domain.services.resident.ResidentCommandService;
import com.ironcoders.aquaconectabackend.subcriptions.infrastructure.persistence.jpa.repositories.resident.ResidentQueryService;
import com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.resources.resident.CreateResidentResource;
import com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.resources.resident.ResidentResource;
import com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.transform.provider.CreateProviderCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/residents", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Residents", description = "Resident Management Endpoints")
public class ResidentController {

    private final ResidentCommandService residentCommandService;
    //private final ResidentQueryService residentQueryService;


    public ResidentController(ResidentCommandService residentCommandService /*, ResidentQueryService residentQueryService */) {
        this.residentCommandService = residentCommandService;
       // this.residentQueryService = residentQueryService;
    }

    @PostMapping
    public ResponseEntity<ResidentResource> createResident(@RequestBody CreateResidentResource resource){
        CreateResidentCommand createResidentCommand= CreateResidentCommandFromResourceAssembler.toCommandFromResource(resource);
        var resident = residentCommandService.handle(createResidentCommand);
        if (resident.isEmpty())return ResponseEntity.badRequest().build();
        var residentResource = ResidentResourceFromEntityAssembler.toResourceFromEntity(resident.get());
        return new ResponseEntity<>(residentResource, HttpStatus.CREATED);
    }


}
