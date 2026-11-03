package com.example.RPG_Manager20.Model.DTO.Response;

import com.example.RPG_Manager20.Model.Entities.Pericia;
import com.example.RPG_Manager20.Model.Entities.Personagem;
import com.example.RPG_Manager20.Model.Entities.PersonagemPericia;
import com.example.RPG_Manager20.Model.Enums.Atributos;

public record PericiaResponseDTO(
        String slug,
        String nomeExibicao,
        Atributos atributoChave,
        boolean proficiente,
        int modificador,
        int bonusProficiencia,
        int valorTotalPericia
) {

    // ============================================
    // Perícia do personagem (proficiente)
    // ============================================
    public static PericiaResponseDTO from(PersonagemPericia pp,
                                          Personagem personagem,
                                          int bonusProf) {
        Pericia pericia = pp.getPericia();
        int mod = calcularModificador(pericia.getAtributoChave(), personagem);
        int bonus = pp.isProficiente() ? bonusProf : 0;

        return new PericiaResponseDTO(
                pericia.getSlug(),
                pericia.getNomeExibicao(),
                pericia.getAtributoChave(),
                pp.isProficiente(),
                mod,
                bonus,
                mod + bonus
        );
    }

    // ============================================
    // Perícia do catálogo que o personagem NÃO tem
    // ============================================
    public static PericiaResponseDTO naoProficiente(Pericia pericia, Personagem personagem) {
        int mod = calcularModificador(pericia.getAtributoChave(), personagem);
        return new PericiaResponseDTO(
                pericia.getSlug(),
                pericia.getNomeExibicao(),
                pericia.getAtributoChave(),
                false,
                0,
                0,
                mod
        );
    }

    private static int calcularModificador(Atributos attr, Personagem p) {
        int valor = switch (attr) {
            case FORCA        -> p.getValorForca();
            case DESTREZA     -> p.getValorDestreza();
            case CONSTITUICAO -> p.getValorConstituicao();
            case INTELIGENCIA -> p.getValorInteligencia();
            case SABEDORIA    -> p.getValorSabedoria();
            case CARISMA      -> p.getValorCarisma();
        };
        return Math.floorDiv(valor - 10, 2);
    }
}