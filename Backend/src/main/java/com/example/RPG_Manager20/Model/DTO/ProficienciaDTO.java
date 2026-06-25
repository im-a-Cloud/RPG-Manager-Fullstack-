package com.example.RPG_Manager20.Model.DTO;

import com.example.RPG_Manager20.Model.Enums.TipoProficiencia;

public record ProficienciaDTO(
        TipoProficiencia tipoProficiencia,
        String listaProficiencias
) {
}
