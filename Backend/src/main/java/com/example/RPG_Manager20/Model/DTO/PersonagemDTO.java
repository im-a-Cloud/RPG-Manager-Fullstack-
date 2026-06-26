package com.example.RPG_Manager20.Model.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record PersonagemDTO(
        String nomePersonagem,

        @Min(value = 1, message = "Nível deve ser entre 1 e 20")
        @Max(value = 20, message = "Nível deve ser entre 1 e 20")
        int nivelPersonagem,

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
        int valorCarisma,

        int caPersonagem,
        int iniciativaPersonagem,
        int movimentoPersonagem,
        int pontosVidaPersonagem,

        // Campos de texto (opcionais)
        String historiaPersonagem,
        String aparenciaPersonagem,
        String ideaisPersonagem,
        String defeitosPersonagem,
        String anotacoesPersonagem,
        String personalidadePersonagem,
        String racaPersonagem,
        String escalaPersonagem,
        String alinhamentoPersonagem,

        // Campos numéricos
        Double pesoPersonagem,
        Double alturaPersonagem
) {
    // Métodos auxiliares para calcular bônus
    public int getBonusForca() {
        return (valorForca - 10) / 2;
    }

    public int getBonusDestreza() {
        return (valorDestreza - 10) / 2;
    }

    public int getBonusConstituicao() {
        return (valorConstituicao - 10) / 2;
    }

    public int getBonusInteligencia() {
        return (valorInteligencia - 10) / 2;
    }

    public int getBonusSabedoria() {
        return (valorSabedoria - 10) / 2;
    }

    public int getBonusCarisma() {
        return (valorCarisma - 10) / 2;
    }

    public int getBonusProficiencia() {
        return Math.round((nivelPersonagem+3)/4) + 1;
    }
}
