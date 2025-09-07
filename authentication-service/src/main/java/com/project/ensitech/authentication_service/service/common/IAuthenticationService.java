package com.project.ensitech.authentication_service.service.common;

import com.project.ensitech.authentication_service.model.dto.*;

public interface IAuthenticationService {
   // AuthenticationResponse register(RegisterRequest req);
    String register(RegisterRequestDto req);
    AuthenticationResponseDto login(LoginRequestDto req);
    String updatePassword(UpdatePasswordRequestDto request);
    String updateEmail(Integer userId, UpdateEmailRequestDto request);
}
