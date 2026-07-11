package com.foodlink.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record DonacionRealizada(UUID loteId, UUID organizacionId, LocalDateTime ocurridoEn) {
}
