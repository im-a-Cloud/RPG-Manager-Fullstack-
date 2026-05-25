package com.example.RPG_Manager20.Model.DTO.Request;

import com.example.RPG_Manager20.Model.Enums.OrigemHabilidade;

public record HabilidadeRequestDTO(
        String nomeHabilidade,
        String descricaoHabilidade,
        OrigemHabilidade origemHabilidade,
        String usosHabilidade,
        String recargaHabilidade
) {
}
