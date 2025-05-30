package com.ironcoders.aquaconectabackend.management.interfaces.rest.transform.request;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.CreateRequestCommand;
import com.ironcoders.aquaconectabackend.management.domain.services.RequestCommandService;
import com.ironcoders.aquaconectabackend.management.domain.services.RequestQueryService;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.request.CreateRequestResource;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.request.RequestResource;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/requests")
@Tag(name = "Requests", description = "Request Management endpoints")
public class RequestController {

    private final RequestCommandService requestCommandService;
    private final RequestQueryService requestQueryService;

    public RequestController(RequestCommandService requestCommandService, RequestQueryService requestQueryService) {
        this.requestCommandService = requestCommandService;
        this.requestQueryService = requestQueryService;
    }

    @PostMapping
    public ResponseEntity<RequestResource> createRequest(@RequestBody CreateRequestResource resource)
    {
        CreateRequestCommand createRequestCommand = CreateRequestCommandFromResourceAssembler.toCommandFromResource(resource);
        var request = requestCommandService.handle(createRequestCommand);
        if (request.isEmpty())  return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        var requestResource = RequestResourceFromEntityAssembler.toResourceFromEntity(request.get());
        return new ResponseEntity<>(requestResource, HttpStatus.CREATED);

    }
   
}