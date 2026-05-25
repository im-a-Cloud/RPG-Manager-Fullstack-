package com.example.RPG_Manager20.Model.DTO.Request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PersonagemRequestDTO(
        @NotBlank(message = "Nome é obrigatório")
        String nomePersonagem,

        @Min(value = 1, message = "Nível deve ser entre 1 e 20")
        @Max(value = 20, message = "Nível deve ser entre 1 e 20")
        int nivelPersonagem,

        @NotNull(message = "ID da classe é obrigatório")
        Long classeId,

        @Min(value = 1, message = "Força deve ser entre 1 e 20")
        @Max(value = 20, message = "Força deve ser entre 1 e 20")
        int valorForca,

        @Min(value = 1, message = "Destreza deve ser entre 1 e 20")
        @Max(value = 20, message = "Destreza deve ser entre 1 e 20")
        int valorDestreza,

        @Min(value = 1, message = "Constituição deve ser entre 1 e 20")
        @Max(value = 20, message = "Constituição deve ser entre 1 e 20")
        int valorConstituicao,

        @Min(value = 1, message = "Inteligência deve ser entre 1 e 20")
        @Max(value = 20, message = "Inteligência deve ser entre 1 e 20")
        int valorInteligencia,

        @Min(value = 1, message = "Sabedoria deve ser entre 1 e 20")
        @Max(value = 20, message = "Sabedoria deve ser entre 1 e 20")
        int valorSabedoria,

        @Min(value = 1, message = "Carisma deve ser entre 1 e 20")
        @Max(value = 20, message = "Carisma deve ser entre 1 e 20")
        int valorCarisma
) {
}
