package com.example.RPG_Manager20.Model.DTO.Response;

import com.example.RPG_Manager20.Model.Entities.Classe;
import com.example.RPG_Manager20.Model.Entities.Pericia;
import com.example.RPG_Manager20.Model.Entities.Personagem;
import com.example.RPG_Manager20.Model.Entities.PersonagemPericia;
import com.example.RPG_Manager20.Model.Enums.Atributos;

public record PericiaResponseDTO(
        Long idPericia,
        String nomePericia,
        Atributos atributoChave,
        boolean isProficiente,
        int valorTotal,
        int modificadorAtributo,
        int bonusProficiencia
) {
    public static  PericiaResponseDTO from(PersonagemPericia personagemPericia, PersonagemResponseDTO personagemDTO) {

        Pericia pericia = personagemPericia.getPericia();

        int modificador = switch (pericia.getAtributoChave()) {
            case FORCA -> personagemDTO.atributos().getBonusForca();
            case DESTREZA -> personagemDTO.atributos().getBonusDestreza();
            case CONSTITUICAO -> personagemDTO.atributos().getBonusConstituicao();
            case INTELIGENCIA -> personagemDTO.atributos().getBonusInteligencia();
            case SABEDORIA -> personagemDTO.atributos().getBonusSabedoria();
            case CARISMA -> personagemDTO.atributos().getBonusCarisma();
        };

        int bonusProf = personagemDTO.magia().bonusProficiencia();

        int valorTotal = modificador;

        if (personagemPericia.isProficiente()) {
            valorTotal += bonusProf;
        }

        return new PericiaResponseDTO(
                pericia.getId(),
                pericia.getNomePericia(),
                pericia.getAtributoChave(),
                personagemPericia.isProficiente(),
                valorTotal,
                modificador,
                bonusProf
        );

    }
}
