package com.ironcoders.aquaconectabackend.management.domain.model.commads;

import java.time.LocalDateTime;

public record UpdateWaterRequestCommand(
        Long id,                        // ID de la solicitud
        String status,                 // Nuevo estado (por ejemplo: "ENTREGADO")
        LocalDateTime deliveredAt      // Fecha de entrega que asigna el proveedor
) {
}
