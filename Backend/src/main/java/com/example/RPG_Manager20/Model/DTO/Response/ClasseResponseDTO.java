package com.example.RPG_Manager20.Model.DTO.Response;

import com.example.RPG_Manager20.Model.Enums.Classes;
import com.example.RPG_Manager20.Model.Enums.DadosDeVida;

public record ClasseResponseDTO(
        Long id,
        String nome,
        String nomeIngles,
        DadosDeVida dadoDeVida,
        Boolean isConjurador,
        String atributoConjuracao,
        String tipoConjuracao,
        String tag
) {
    public static ClasseResponseDTO fromClasse(Classes classe, Long id) {
        return new ClasseResponseDTO(
                id,
                classe.getNomePortugues(),
                classe.getTagJson(),
                classe.getDadoDeVida(),
                classe.isConjurador(),
                classe.getAtributoConjuracao() != null ?
                        classe.getAtributoConjuracao().name() : null,
                classe.getTipoConjuracao() != null ?
                        classe.getTipoConjuracao().name() : null,
                classe.name()
        );
    }

    // 🔥 MÉTODO FACTORY COM DADOS ADICIONAIS (OPCIONAL)
    public static ClasseResponseDTO fromClasse(Classes classe, Long id, String nomePersonalizado) {
        return new ClasseResponseDTO(
                id,
                nomePersonalizado != null ? nomePersonalizado : classe.getNomePortugues(),
                classe.getTagJson(),
                classe.getDadoDeVida(),
                classe.isConjurador(),
                classe.getAtributoConjuracao() != null ?
                        classe.getAtributoConjuracao().name() : null,
                classe.getTipoConjuracao() != null ?
                        classe.getTipoConjuracao().name() : null,
                classe.name()
        );
    }
}