package com.example.RPG_Manager20.Model.DTO;

import com.example.RPG_Manager20.Model.Entities.Proficiencia;
import com.example.RPG_Manager20.Model.Enums.Atributos;
import com.example.RPG_Manager20.Model.Enums.Classes;
import com.example.RPG_Manager20.Model.Enums.TipoConjuracao;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ClasseDTO(
        @NotNull(message = "Nome da classe é obrigatório")
        Classes nomeClasse,

        @Min(value = 6, message = "Dado de vida deve ser no mínimo 6")
        int dadoDeVida,

        List<Proficiencia> listaProficienciasClasse,
        List<Atributos> proficienciaSalvaguarda,

        // Novos campos
        @JsonProperty("conjurador")  // ← Mapeia o campo JSON
        boolean isConjurador,

        Atributos atributoConjuracao,

        TipoConjuracao tipoConjuracao
) {

    public boolean isConjurador() {
        return isConjurador;
    }

    public boolean isConjuradorCompleto() {
        return tipoConjuracao == TipoConjuracao.COMPLETO;
    }

    public boolean isMeioConjurador() {
        return tipoConjuracao == TipoConjuracao.MEIO;
    }

    public boolean isTerciarioConjurador() {
        return tipoConjuracao == TipoConjuracao.TERCIARIO;
    }
}