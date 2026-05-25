package com.example.RPG_Manager20.Model.DTO;

import com.example.RPG_Manager20.Model.Enums.OrigemHabilidade;

public record HabilidadeDTO(
        String nomeHabilidade,
        String descricaoHabilidade,
        OrigemHabilidade origemHabilidade,
        String usosHabilidade,
        String recargaHabilidade
) {}
