package com.foodlink.domain.port.input;

import com.foodlink.application.dto.request.LoginRequest;
import com.foodlink.application.dto.response.AuthResponse;

public interface LoginUseCase {

    AuthResponse login(LoginRequest request);

    AuthResponse refresh(String refreshToken);

    void logout(String refreshToken);
}
