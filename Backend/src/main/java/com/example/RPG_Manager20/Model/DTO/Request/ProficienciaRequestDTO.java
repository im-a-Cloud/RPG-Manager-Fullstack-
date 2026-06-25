package com.example.RPG_Manager20.Model.DTO.Request;

import com.example.RPG_Manager20.Model.Enums.TipoProficiencia;

public record ProficienciaRequestDTO(
        TipoProficiencia tipoProficiencia,
        String listaProficiencias
) {
}
