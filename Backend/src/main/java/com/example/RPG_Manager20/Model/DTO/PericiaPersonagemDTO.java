package com.example.RPG_Manager20.Model.DTO;

import com.example.RPG_Manager20.Model.Entities.Personagem;
import com.example.RPG_Manager20.Model.Entities.PersonagemPericia;

public record PericiaPersonagemDTO(
        String nomePericia,
        Boolean isProficiente,
        Integer valorTotalPericia
) {
    public PericiaPersonagemDTO {
        if (isProficiente == null) isProficiente = false;
        if (valorTotalPericia == null) valorTotalPericia = 0;
    }

    // 🔥 MÉTODO FROM PARA O RESPONSE
    public static PericiaPersonagemDTO from(PersonagemPericia pp, Personagem personagem, int bonusProficiencia) {
        if (pp == null || pp.getPericia() == null) {
            return new PericiaPersonagemDTO(null, false, 0);
        }

        int modificador = switch (pp.getPericia().getAtributoChave()) {
            case FORCA        -> (personagem.getValorForca()        - 10) / 2;
            case DESTREZA     -> (personagem.getValorDestreza()     - 10) / 2;
            case CONSTITUICAO -> (personagem.getValorConstituicao() - 10) / 2;
            case INTELIGENCIA -> (personagem.getValorInteligencia() - 10) / 2;
            case SABEDORIA    -> (personagem.getValorSabedoria()    - 10) / 2;
            case CARISMA      -> (personagem.getValorCarisma()      - 10) / 2;
        };

        int valorTotal = modificador;
        if (pp.isProficiente()) {
            valorTotal += bonusProficiencia;
        }

        return new PericiaPersonagemDTO(
                pp.getPericia().getNomeExibicao(),
                pp.isProficiente(),
                valorTotal
        );
    }
}