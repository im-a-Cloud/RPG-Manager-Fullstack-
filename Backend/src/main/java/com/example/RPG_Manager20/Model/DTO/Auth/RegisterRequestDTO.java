package com.example.RPG_Manager20.Model.DTO.Auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(
        @Email @NotBlank String email,
        @NotBlank @Size(min = 6) String senha,
        @NotBlank String nome
) {
}
