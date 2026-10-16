package com.example.RPG_Manager20.Controller;

import com.example.RPG_Manager20.Model.DTO.Auth.LoginRequestDTO;
import com.example.RPG_Manager20.Model.DTO.Auth.RegisterRequestDTO;
import com.example.RPG_Manager20.Model.DTO.Auth.TokenResponseDTO;
import com.example.RPG_Manager20.Service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<TokenResponseDTO> registrar(@Valid @RequestBody RegisterRequestDTO dto) {
        return ResponseEntity.ok(authService.registrar(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }
}
