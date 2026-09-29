package com.example.RPG_Manager20.Model.DTO.Request;

import com.example.RPG_Manager20.Model.DTO.HabilidadeDTO;
import com.example.RPG_Manager20.Model.DTO.ItemDTO;
import com.example.RPG_Manager20.Model.DTO.MagiaDTO;
import com.example.RPG_Manager20.Model.DTO.ProficienciaDTO;
import com.example.RPG_Manager20.Model.DTO.Request.PericiaPersonagemRequestDTO;
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
        Integer nivelPersonagem,

        @NotNull(message = "ID da classe é obrigatório")
        Long classeId,

        // ============ ATRIBUTOS ============
        @Min(value = 1, message = "Força deve ser entre 1 e 20")
        @Max(value = 20, message = "Força deve ser entre 1 e 20")
        Integer valorForca,

        @Min(value = 1, message = "Destreza deve ser entre 1 e 20")
        @Max(value = 20, message = "Destreza deve ser entre 1 e 20")
        Integer valorDestreza,

        @Min(value = 1, message = "Constituição deve ser entre 1 e 20")
        @Max(value = 20, message = "Constituição deve ser entre 1 e 20")
        Integer valorConstituicao,

        @Min(value = 1, message = "Inteligência deve ser entre 1 e 20")
        @Max(value = 20, message = "Inteligência deve ser entre 1 e 20")
        Integer valorInteligencia,

        @Min(value = 1, message = "Sabedoria deve ser entre 1 e 20")
        @Max(value = 20, message = "Sabedoria deve ser entre 1 e 20")
        Integer valorSabedoria,

        @Min(value = 1, message = "Carisma deve ser entre 1 e 20")
        @Max(value = 20, message = "Carisma deve ser entre 1 e 20")
        Integer valorCarisma,

        // ============ COMBATE ============
        Integer ca,
        Integer iniciativa,
        Integer pontosVida,
        Integer movimento,

        // ============ DESCRITIVOS ============
        String historiaPersonagem,
        String aparenciaPersonagem,
        String ideaisPersonagem,
        String defeitosPersonagem,
        String anotacoesPersonagem,
        String personalidadePersonagem,

        // ============ DADOS BIOGRÁFICOS ============
        String racaPersonagem,
        String escalaPersonagem,
        String alinhamentoPersonagem,
        Integer idadePersonagem,          // ← ADICIONADO
        String generoPersonagem,         // ← ADICIONADO
        String antecedentePersonagem,    // ← ADICIONADO (se usar)

        // ============ NUMÉRICOS ============
        Double pesoPersonagem,
        Double alturaPersonagem,

        // ============ FOTO ============
        String fotoBase64,

        // ============ LISTAS ============
        @Valid
        List<HabilidadeDTO> habilidades,
        @Valid
        List<ProficienciaDTO> proficiencias,
        @Valid
        List<ItemDTO> inventario,
        @Valid
        List<MagiaDTO> magias,
        @Valid
        List<PericiaPersonagemRequestDTO> pericias
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

        // Strings descritivas vazias
        if (historiaPersonagem == null) historiaPersonagem = "";
        if (aparenciaPersonagem == null) aparenciaPersonagem = "";
        if (ideaisPersonagem == null) ideaisPersonagem = "";
        if (defeitosPersonagem == null) defeitosPersonagem = "";
        if (anotacoesPersonagem == null) anotacoesPersonagem = "";
        if (personalidadePersonagem == null) personalidadePersonagem = "";
        if (racaPersonagem == null) racaPersonagem = "";
        if (escalaPersonagem == null) escalaPersonagem = "";
        if (alinhamentoPersonagem == null) alinhamentoPersonagem = "";
        if (idadePersonagem == null) idadePersonagem = 0;
        if (generoPersonagem == null) generoPersonagem = "";
        if (antecedentePersonagem == null) antecedentePersonagem = "";
        if (fotoBase64 == null) fotoBase64 = "";

        // Listas
        if (proficiencias == null) proficiencias = List.of();
        if (pericias == null) pericias = List.of();
        if (habilidades == null) habilidades = List.of();
        if (inventario == null) inventario = List.of();
        if (magias == null) magias = List.of();
    }
}