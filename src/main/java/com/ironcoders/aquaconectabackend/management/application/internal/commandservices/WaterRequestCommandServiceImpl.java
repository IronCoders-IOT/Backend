package com.ironcoders.aquaconectabackend.management.application.internal.commandservices;

import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.WaterRequestAggregate;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.CreateWaterRequestCommand;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.UpdateWaterRequestCommand;
import com.ironcoders.aquaconectabackend.management.domain.services.WaterRequestCommandService;
import com.ironcoders.aquaconectabackend.management.infrastructure.persistence.jpa.repositories.WaterRequestRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class WaterRequestCommandServiceImpl implements WaterRequestCommandService {

    private final WaterRequestRepository waterRequestRepository;

    public WaterRequestCommandServiceImpl(WaterRequestRepository waterRequestRepository) {
        this.waterRequestRepository = waterRequestRepository;
    }

    @Override

    public Optional<WaterRequestAggregate> handle(CreateWaterRequestCommand command){

        var waterRequestAggregate = new WaterRequestAggregate(command);
        waterRequestRepository.save(waterRequestAggregate);
        return Optional.of(waterRequestAggregate);


    }



    @Override
    public Optional<WaterRequestAggregate> handle(UpdateWaterRequestCommand command) {

        return Optional.empty();

    }


}