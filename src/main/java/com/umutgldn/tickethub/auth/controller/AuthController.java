package com.umutgldn.tickethub.auth.controller;

import com.umutgldn.tickethub.auth.dto.AuthResponse;
import com.umutgldn.tickethub.auth.dto.LoginRequest;
import com.umutgldn.tickethub.auth.dto.LogoutRequest;
import com.umutgldn.tickethub.auth.dto.RefreshRequest;
import com.umutgldn.tickethub.auth.service.AuthService;
import com.umutgldn.tickethub.common.util.RequestUtils;
import com.umutgldn.tickethub.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest
    ) {
        AuthResponse authResponse = authService.login(
                request,
                RequestUtils.extractDeviceInfo(httpRequest),
                RequestUtils.extractIpAddress(httpRequest)
        );
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @Valid @RequestBody RefreshRequest request,
            HttpServletRequest httpRequest
    ){
        AuthResponse authResponse = authService.refresh(
                request,
                RequestUtils.extractDeviceInfo(httpRequest),
                RequestUtils.extractIpAddress(httpRequest)
        );
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody LogoutRequest request){
        authService.logout(request.refreshToken());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/logout-all")
    public ResponseEntity<Void> logoutAll(@AuthenticationPrincipal User user){
        authService.logoutAll(user);
        return ResponseEntity.noContent().build();
    }

}
