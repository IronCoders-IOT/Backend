package com.ironcoders.aquaconectabackend.management.application.internal.commandservices;

import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.WaterRequestAggregate;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.CreateWaterRequestCommand;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.DeleteWaterRequestCommand;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.UpdateWaterRequestCommand;
import com.ironcoders.aquaconectabackend.management.domain.services.WaterRequestCommandService;
import com.ironcoders.aquaconectabackend.management.infrastructure.persistence.jpa.repositories.WaterRequestRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class WaterRequestCommandServiceImpl implements WaterRequestCommandService {

    private final WaterRequestRepository waterRequestRepository;

    public WaterRequestCommandServiceImpl(WaterRequestRepository waterRequestRepository) {
        this.waterRequestRepository = waterRequestRepository;
    }

    @Override
    public void handle(CreateWaterRequestCommand command) {
        WaterRequestAggregate req = new WaterRequestAggregate(
                command.residentId(),
                command.providerId(),
                command.requestedLiters(),
                command.status(),
                command.deliveredAt()
        );
        waterRequestRepository.save(req);
    }

    @Override
    public void handle(UpdateWaterRequestCommand command) {
        WaterRequestAggregate req = waterRequestRepository.findById(command.waterRequestId())
                .orElseThrow(() -> new RuntimeException("WaterRequest not found"));
        req.update(
                command.requestedLiters(),
                command.status(),
                command.deliveredAt()
        );
        waterRequestRepository.save(req);
    }

    @Override
    public void handle(DeleteWaterRequestCommand command) {
        waterRequestRepository.deleteById(command.waterRequestId());
    }
}