package com.example.RPG_Manager20.Model.DTO.Response;

import com.example.RPG_Manager20.Model.Entities.Pericia;
import com.example.RPG_Manager20.Model.Entities.PersonagemPericia;
import com.example.RPG_Manager20.Model.Enums.Atributos;

public record PericiaResponseDTO(
        Long id,
        String slug,
        String nomeExibicao,
        Atributos atributoChave,
        Boolean isProficiente,
        Integer modificador,
        Integer bonusProficiencia,
        Integer valorTotalPericia
) {
    // ============================================
    // FROM — só a Pericia (catálogo)
    // ============================================
    public static PericiaResponseDTO from(Pericia p) {
        if (p == null) {
            return new PericiaResponseDTO(null, null, null, null, false, 0, 0, 0);
        }
        return new PericiaResponseDTO(
                p.getId(),
                p.getSlug(),
                p.getNomeExibicao(),
                p.getAtributoChave(),
                false,
                0,
                0,
                0
        );
    }

    // ============================================
    // FROM — PersonagemPericia + PersonagemResponseDTO
    // ============================================
    public static PericiaResponseDTO from(
            PersonagemPericia pp,
            PersonagemResponseDTO personagemDTO) {

        if (pp == null || pp.getPericia() == null) {
            return new PericiaResponseDTO(null, null, null, null, false, 0, 0, 0);
        }

        Pericia pericia = pp.getPericia();

        // Lê os valores do personagem pelo DTO
        int valorForca        = personagemDTO.atributos().forca();
        int valorDestreza     = personagemDTO.atributos().destreza();
        int valorConstituicao = personagemDTO.atributos().constituicao();
        int valorInteligencia = personagemDTO.atributos().inteligencia();
        int valorSabedoria    = personagemDTO.atributos().sabedoria();
        int valorCarisma      = personagemDTO.atributos().carisma();

        int modificador = switch (pericia.getAtributoChave()) {
            case FORCA        -> (valorForca        - 10) / 2;
            case DESTREZA     -> (valorDestreza     - 10) / 2;
            case CONSTITUICAO -> (valorConstituicao - 10) / 2;
            case INTELIGENCIA -> (valorInteligencia - 10) / 2;
            case SABEDORIA    -> (valorSabedoria    - 10) / 2;
            case CARISMA      -> (valorCarisma      - 10) / 2;
        };

        int bonusProf = personagemDTO.magia() != null
                ? personagemDTO.magia().bonusProficiencia()
                : 0;

        int bonus = pp.isProficiente() ? bonusProf : 0;
        int valorTotal = modificador + bonus;

        return new PericiaResponseDTO(
                pericia.getId(),
                pericia.getSlug(),
                pericia.getNomeExibicao(),
                pericia.getAtributoChave(),
                pp.isProficiente(),
                modificador,
                bonus,
                valorTotal
        );
    }
}