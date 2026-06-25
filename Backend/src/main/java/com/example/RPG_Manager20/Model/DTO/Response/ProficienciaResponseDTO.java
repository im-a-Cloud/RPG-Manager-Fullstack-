package com.example.RPG_Manager20.Model.DTO.Response;

import com.example.RPG_Manager20.Model.Enums.TipoProficiencia;

public record ProficienciaResponseDTO
        (TipoProficiencia tipoProficiencia,
         String listaProficiencias){
}
