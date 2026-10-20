package com.example.RPG_Manager20.Model.DTO.Response;

import com.example.RPG_Manager20.Model.Entities.Usuario;

public record UsuarioResponseDTO(
        Long id,
        String nome,
        String email
) {
    public static UsuarioResponseDTO from(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNomeUsuario(),
                usuario.getEmailUsuario()
        );
    }
}