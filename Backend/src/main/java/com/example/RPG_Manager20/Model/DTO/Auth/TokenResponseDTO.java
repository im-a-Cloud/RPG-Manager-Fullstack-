package com.example.RPG_Manager20.Model.DTO.Auth;

public record TokenResponseDTO(
        String token,
        Long usuarioId,
        String email,
        String nome
) {
}
