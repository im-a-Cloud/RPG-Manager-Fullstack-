package com.example.RPG_Manager20.Model.DTO.Summary;

import com.example.RPG_Manager20.Model.Enums.Classes;

public record PersonagemSummaryDTO(
        Long id,
        String nomePersonagem,
        int nivelPersonagem,
        Classes nomeClasse
) {
}
