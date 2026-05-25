package com.example.RPG_Manager20.Model.DTO;

import com.example.RPG_Manager20.Model.Enums.Atributos;
import com.example.RPG_Manager20.Model.Entities.Personagem;
import com.example.RPG_Manager20.Model.Entities.PersonagemPericia;

public record PericiaPersonagemDTO(
        Long id,
        String nomePericia,
        Atributos atributoChave,
        boolean isProficiente,
        int valorTotal
) {

    public static PericiaPersonagemDTO from(PersonagemPericia pp, Personagem personagem, int bonusProficiencia) {
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

        return new PericiaPersonagemDTO(
                pp.getPericia().getId(),
                pp.getPericia().getNomePericia(),
                pp.getPericia().getAtributoChave(),
                pp.isProficiente(),
                valorTotal
        );
    }
}