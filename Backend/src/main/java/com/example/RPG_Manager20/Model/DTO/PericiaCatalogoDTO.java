package com.example.RPG_Manager20.Model.DTO;

import com.example.RPG_Manager20.Model.Entities.Pericia;
import com.example.RPG_Manager20.Model.Enums.Atributos;

public record PericiaCatalogoDTO(
        Long id,
        String slug,
        String nomeExibicao,
        Atributos atributoChave
) {
    public static PericiaCatalogoDTO from(Pericia p) {
        if (p == null) return null;
        return new PericiaCatalogoDTO(
                p.getId(),
                p.getSlug(),
                p.getNomeExibicao(),
                p.getAtributoChave()
        );
    }
}
