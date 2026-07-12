package com.foodlink.application.dto.request;

import java.util.UUID;

public record VerificarActorRequest(
        UUID actorId,
        String accion
) {
}