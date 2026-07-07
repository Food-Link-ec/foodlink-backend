package com.foodlink.application.dto.request;

public record LoginRequest(
        String email,
        String password
) {
}
