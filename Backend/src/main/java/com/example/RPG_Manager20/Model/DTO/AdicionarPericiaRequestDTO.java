package com.example.RPG_Manager20.Model.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;

public record AdicionarPericiaRequestDTO(
        @NotNull(message = "ID da perícia é obrigatório")
        @NotNull
        @JsonIgnoreProperties(ignoreUnknown = true)
        Long periciaId,
        boolean isProficiente
) {
}
