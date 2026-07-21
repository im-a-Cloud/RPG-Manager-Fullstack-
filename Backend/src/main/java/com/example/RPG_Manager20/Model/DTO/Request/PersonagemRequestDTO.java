package com.example.RPG_Manager20.Model.DTO.Request;

import com.example.RPG_Manager20.Model.DTO.*;
import com.example.RPG_Manager20.Model.Entities.Pericia;
import com.example.RPG_Manager20.Model.Entities.Proficiencia;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PersonagemRequestDTO(
        @NotBlank(message = "Nome é obrigatório")
        String nomePersonagem,

        @Min(value = 1, message = "Nível deve ser entre 1 e 20")
        @Max(value = 20, message = "Nível deve ser entre 1 e 20")
        Integer nivelPersonagem,  // ← Mude para Integer

        @NotNull(message = "ID da classe é obrigatório")
        Long classeId,

        @Min(value = 1, message = "Força deve ser entre 1 e 20")
        @Max(value = 20, message = "Força deve ser entre 1 e 20")
        Integer valorForca,  // ← Mude para Integer

        @Min(value = 1, message = "Destreza deve ser entre 1 e 20")
        @Max(value = 20, message = "Destreza deve ser entre 1 e 20")
        Integer valorDestreza,  // ← Mude para Integer

        @Min(value = 1, message = "Constituição deve ser entre 1 e 20")
        @Max(value = 20, message = "Constituição deve ser entre 1 e 20")
        Integer valorConstituicao,  // ← Mude para Integer

        @Min(value = 1, message = "Inteligência deve ser entre 1 e 20")
        @Max(value = 20, message = "Inteligência deve ser entre 1 e 20")
        Integer valorInteligencia,  // ← Mude para Integer

        @Min(value = 1, message = "Sabedoria deve ser entre 1 e 20")
        @Max(value = 20, message = "Sabedoria deve ser entre 1 e 20")
        Integer valorSabedoria,  // ← Mude para Integer

        @Min(value = 1, message = "Carisma deve ser entre 1 e 20")
        @Max(value = 20, message = "Carisma deve ser entre 1 e 20")
        Integer valorCarisma,  // ← Mude para Integer

        // Combate
        Integer ca,  // ← Mude para Integer
        Integer iniciativa,  // ← Mude para Integer
        Integer pontosVida,  // ← Mude para Integer
        Integer movimento,  // ← Mude para Integer

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
        Double pesoPersonagem,  // ← Mude para Double
        Double alturaPersonagem,  // ← Mude para Double

        // Listas
        @Valid
        List<HabilidadeDTO> habilidades,
        @Valid
        List<ProficienciaDTO> proficiencias,
        @Valid
        List<ItemDTO> inventario,
        @Valid
        List<MagiaDTO> magias,
        @Valid
        List<PericiaPersonagemDTO> pericias
) {
    // Construtor com valores padrão
    public PersonagemRequestDTO {
        if (nivelPersonagem == null) nivelPersonagem = 1;
        if (valorForca == null) valorForca = 10;
        if (valorDestreza == null) valorDestreza = 10;
        if (valorConstituicao == null) valorConstituicao = 10;
        if (valorInteligencia == null) valorInteligencia = 10;
        if (valorSabedoria == null) valorSabedoria = 10;
        if (valorCarisma == null) valorCarisma = 10;
        if (ca == null) ca = 10;
        if (iniciativa == null) iniciativa = 0;
        if (pontosVida == null) pontosVida = 10;
        if (movimento == null) movimento = 9;
        if (pesoPersonagem == null) pesoPersonagem = 0.0;
        if (alturaPersonagem == null) alturaPersonagem = 0.0;
        if (proficiencias == null) proficiencias = List.of();
        if (pericias == null) pericias = List.of();
        if (habilidades == null) habilidades = List.of();
    }
}