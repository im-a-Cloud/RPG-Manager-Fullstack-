
package com.example.RPG_Manager20.Model.DTO;

import com.example.RPG_Manager20.Model.Enums.Atributos;
import com.example.RPG_Manager20.Model.Entities.Personagem;
import com.example.RPG_Manager20.Model.Entities.PersonagemPericia;

public record PericiaPersonagemDTO(
        Long personagemId,
        PericiaDTO pericia,
        Boolean isProficiente,
        Integer bonusPersonalizado
) {
    public PericiaPersonagemDTO {
        if (isProficiente == null) isProficiente = false;
        if (bonusPersonalizado == null) bonusPersonalizado = 0;
    }

    public static PericiaPersonagemDTO from(PersonagemPericia pp, Personagem personagem, int bonusProficiencia) {
        if (pp.getPericia() == null) {
            return new PericiaPersonagemDTO(
                    personagem.getId(),
                    null,
                    false,
                    0
            );
        }

        int modificador = switch (pp.getPericia().getAtributoChave()) {
            case FORCA -> (personagem.getValorForca() - 10) / 2;
            case DESTREZA -> (personagem.getValorDestreza() - 10) / 2;
            case CONSTITUICAO -> (personagem.getValorConstituicao() - 10) / 2;
            case INTELIGENCIA -> (personagem.getValorInteligencia() - 10) / 2;
            case SABEDORIA -> (personagem.getValorSabedoria() - 10) / 2;
            case CARISMA -> (personagem.getValorCarisma() - 10) / 2;
        };

        int valorTotal = modificador;
        if (pp.isProficiente()) {
            valorTotal += bonusProficiencia;
        }

        // 🔥 CORRETO: USANDO OS MÉTODOS DO RECORD (SEM "get")
        PericiaDTO periciaDTO = new PericiaDTO(
                pp.getPericia().getId(),
                pp.getPericia().getNomePericia(),
                pp.getPericia().getAtributoChave()
        );

        return new PericiaPersonagemDTO(
                personagem.getId(),
                periciaDTO,
                pp.isProficiente(),
                valorTotal
        );
    }
}