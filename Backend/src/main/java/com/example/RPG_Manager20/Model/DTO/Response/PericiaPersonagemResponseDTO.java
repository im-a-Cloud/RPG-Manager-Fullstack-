package com.example.RPG_Manager20.Model.DTO.Response;

import com.example.RPG_Manager20.Model.Entities.Pericia;
import com.example.RPG_Manager20.Model.Entities.Personagem;
import com.example.RPG_Manager20.Model.Entities.PersonagemPericia;
import com.example.RPG_Manager20.Model.Enums.Atributos;

public record PericiaPersonagemResponseDTO(
        String slug,
        String nomeExibicao,
        Atributos atributoChave,
        Boolean isProficiente,
        Integer modificador,
        Integer bonusProficiencia,
        Integer valorTotalPericia
) {
    public static PericiaPersonagemResponseDTO from(
            PersonagemPericia pp,
            Personagem personagem,
            int bonusProficiencia) {

        if (pp == null || pp.getPericia() == null) {
            return new PericiaPersonagemResponseDTO(null, null, null, false, 0, 0, 0);
        }

        Pericia pericia = pp.getPericia();

        int modificador = switch (pericia.getAtributoChave()) {
            case FORCA        -> (personagem.getValorForca()        - 10) / 2;
            case DESTREZA     -> (personagem.getValorDestreza()     - 10) / 2;
            case CONSTITUICAO -> (personagem.getValorConstituicao() - 10) / 2;
            case INTELIGENCIA -> (personagem.getValorInteligencia() - 10) / 2;
            case SABEDORIA    -> (personagem.getValorSabedoria()    - 10) / 2;
            case CARISMA      -> (personagem.getValorCarisma()      - 10) / 2;
        };

        int bonus = pp.isProficiente() ? bonusProficiencia : 0;
        int valorTotal = modificador + bonus;

        return new PericiaPersonagemResponseDTO(
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