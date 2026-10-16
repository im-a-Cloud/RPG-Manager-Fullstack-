package com.example.RPG_Manager20.Model.DTO.Auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @Email @NotBlank String email,
        @NotBlank String senha
) {
}
