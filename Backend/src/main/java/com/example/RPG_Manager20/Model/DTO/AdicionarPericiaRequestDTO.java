package com.example.RPG_Manager20.Model.DTO;

import jakarta.validation.constraints.NotNull;

public record AdicionarPericiaRequestDTO(
        @NotNull(message = "ID da perícia é obrigatório")
        Long periciaId,
        boolean isProficiente
) {
}
