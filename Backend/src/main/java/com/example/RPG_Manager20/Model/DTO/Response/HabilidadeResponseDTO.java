package com.example.RPG_Manager20.Model.DTO.Response;

import com.example.RPG_Manager20.Model.Enums.OrigemHabilidade;

public record HabilidadeResponseDTO (
        Long idHabilidade,
        String nomeHabilidade,
        String descricaoHabilidade,
        OrigemHabilidade origemHabilidade,
        String usosHabilidade,
        String recargaHabilidade
){
}
