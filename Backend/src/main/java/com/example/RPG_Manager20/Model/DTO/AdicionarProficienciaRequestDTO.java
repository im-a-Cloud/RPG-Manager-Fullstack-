package com.example.RPG_Manager20.Model.DTO;

import com.example.RPG_Manager20.Model.Enums.TipoProficiencia;
import jakarta.validation.constraints.NotNull;

public record AdicionarProficienciaRequestDTO(
        @NotNull(message = "Tipo da proficiência é obrigatório")
        TipoProficiencia tipoProficiencia,

        @NotNull(message = "Lista de proficiências é obrigatória")
        String listaProficiencias
) {
}
