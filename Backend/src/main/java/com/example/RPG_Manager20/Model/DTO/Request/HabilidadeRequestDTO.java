package com.example.RPG_Manager20.Model.DTO.Request;

import com.example.RPG_Manager20.Model.Enums.OrigemHabilidade;

public record HabilidadeRequestDTO(
        String nomeHabilidade,
        String descricaoHabilidade,
        OrigemHabilidade origemHabilidade,
        String usosHabilidade,
        String recargaHabilidade
) {
    public HabilidadeRequestDTO{
        if (nomeHabilidade == null) nomeHabilidade = "";
        if (descricaoHabilidade == null) descricaoHabilidade = "";
        if (origemHabilidade == null) origemHabilidade = OrigemHabilidade.OUTROS;
        if (usosHabilidade == null) usosHabilidade = "";
        if (recargaHabilidade == null) recargaHabilidade = "";
    }

}
