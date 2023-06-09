package com._8attery.seesaw.controller.auth;

import com._8attery.seesaw.annotation.ClientIp;
import com._8attery.seesaw.dto.api.request.EmailRequestDto;
import com._8attery.seesaw.dto.auth.request.LoginRequestDto;
import com._8attery.seesaw.dto.auth.request.RefreshTokenRequestDto;
import com._8attery.seesaw.dto.auth.response.LoginResponseDto;
import com._8attery.seesaw.service.auth.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthServiceImpl authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto, @ClientIp String clientIp) {
        if (loginRequestDto.getProvider().equalsIgnoreCase("APPLE")) {
            return ResponseEntity.ok(authService.appleLogin(loginRequestDto, clientIp));
        } else if (loginRequestDto.getProvider().equalsIgnoreCase("KAKAO")) {
            return ResponseEntity.ok(authService.kakaoLogin(loginRequestDto, clientIp));
        } else {
            throw new IllegalArgumentException("Provider값이 KAKAO 또는 APPLE이 아닙니다.");
        }
    }

    @PostMapping("/regenerate-token")
    public ResponseEntity<LoginResponseDto> regenerateAccessToken(@Valid @RequestBody RefreshTokenRequestDto refreshTokenRequestDto, @ClientIp String clientIp) {
        return ResponseEntity.ok(authService.regenerateAccessToken(refreshTokenRequestDto, clientIp));
    }

    @PostMapping("/test/generate-token")
    public ResponseEntity<LoginResponseDto> generateAccessTokenByUserId(@RequestBody Long userId) {
        return ResponseEntity.ok(authService.generateAccessTokenByUserId(userId));
    }

    @PostMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@Valid @RequestBody EmailRequestDto emailRequestDto) {
        return ResponseEntity.ok(authService.checkEmail(emailRequestDto.getEmail()));
    }
}
