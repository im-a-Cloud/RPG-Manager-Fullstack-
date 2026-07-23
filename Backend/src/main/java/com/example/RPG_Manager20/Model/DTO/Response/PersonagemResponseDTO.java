package com.example.RPG_Manager20.Model.DTO.Response;

import com.example.RPG_Manager20.Model.DTO.*;
import com.example.RPG_Manager20.Model.Entities.Components;
import com.example.RPG_Manager20.Model.Enums.*;
import com.example.RPG_Manager20.Model.Entities.Personagem;
import com.example.RPG_Manager20.Model.Entities.Classe;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public record PersonagemResponseDTO(
        Long id,
        String nomePersonagem,
        int nivelPersonagem,
        ClasseInfo classe,
        AtributosInfo atributos,
        MagiaInfo magia,
        List<ItemDTO> inventario,
        List<PericiaPersonagemDTO> pericias,
        List<HabilidadeDTO> habilidades,
        List<ProficienciaDTO> proficiencia,
        List<MagiaDTO> magias,  // ← ADICIONADO!

        String historiaPersonagem,
        String aparenciaPersonagem,
        String ideaisPersonagem,
        String defeitosPersonagem,
        String anotacoesPersonagem,
        String personalidadePersonagem,
        String racaPersonagem,
        String escalaPersonagem,
        String alinhamentoPersonagem,
        double pesoPersonagem,
        double alturaPersonagem,
        @JsonProperty("ca") int ca,
        @JsonProperty("iniciativa") int iniciativa,
        @JsonProperty("movimento") int movimento,
        int pontosVidaPersonagem
) {

    public record ClasseInfo(
            Classes nome,
            DadosDeVida dadoDeVida,
            boolean isConjurador,
            Atributos atributoConjuracao,
            TipoConjuracao tipo,
            List<Atributos> proficienciaSalvaguarda
    ) {}

    public record AtributosInfo(
            int forca,
            int destreza,
            int constituicao,
            int inteligencia,
            int sabedoria,
            int carisma
    ) {
        @JsonProperty("bonusForca")
        public int getBonusForca() { return (forca - 10) / 2; }

        @JsonProperty("bonusDestreza")
        public int getBonusDestreza() { return (destreza - 10) / 2; }

        @JsonProperty("bonusConstituicao")
        public int getBonusConstituicao() { return (constituicao - 10) / 2; }

        @JsonProperty("bonusInteligencia")
        public int getBonusInteligencia() { return (inteligencia - 10) / 2; }

        @JsonProperty("bonusSabedoria")
        public int getBonusSabedoria() { return (sabedoria - 10) / 2; }

        @JsonProperty("bonusCarisma")
        public int getBonusCarisma() { return (carisma - 10) / 2; }
    }

    public record MagiaInfo(
            int bonusProficiencia,
            int cd,
            int ataque
    ) {}
    private static ComponentsDTO toComponentsDTO(Components components) {
        if (components == null) {
            return null;
        }
        return new ComponentsDTO(
                components.isVerbal(),
                components.getRaw(),
                components.isSomatic(),
                components.isMaterial()
        );
    }
    // ============================================
    // MÉTODO FACTORY
    // ============================================
    public static PersonagemResponseDTO from(Personagem personagem, Classe classe) {
        if (personagem == null || classe == null) {
            throw new IllegalArgumentException("Personagem e Classe não podem ser nulos");
        }

        int nivel = personagem.getNivelPersonagem();
        int bonusProficiencia = ((nivel + 3) / 4) + 1;
        int bonusDestreza = (personagem.getValorDestreza() - 10) / 2;
        int ca = 10 + bonusDestreza;
        int iniciativa = bonusDestreza;
        int movimento = 9;
        int cd = 0;
        int ataque = 0;

        // ============================================
        // CÁLCULO DE CONJURAÇÃO
        // ============================================
        if (classe.isConjurador() && classe.getAtributoConjuracao() != null) {
            int valorAtributo = switch (classe.getAtributoConjuracao()) {
                case FORCA -> personagem.getValorForca();
                case DESTREZA -> personagem.getValorDestreza();
                case CONSTITUICAO -> personagem.getValorConstituicao();
                case INTELIGENCIA -> personagem.getValorInteligencia();
                case SABEDORIA -> personagem.getValorSabedoria();
                case CARISMA -> personagem.getValorCarisma();
            };
            int modificadorConjuracao = (valorAtributo - 10) / 2;

            cd = 8 + bonusProficiencia + modificadorConjuracao;
            ataque = bonusProficiencia + modificadorConjuracao;
        }

        // ============================================
        // 🔥 CONVERTER INVENTÁRIO (COM VERIFICAÇÃO DE NULL)
        // ============================================
        List<ItemDTO> inventarioDTO = personagem.getInventarioPersonagem() != null
                ? personagem.getInventarioPersonagem().stream()
                .map(item -> new ItemDTO(
                        item.getNomeItem(),
                        item.getDescricaoItem(),
                        item.getPrecoItem(),
                        item.getRaridadeItem(),
                        item.getPesoItem(),
                        item.isMagico(),
                        item.isPrecisaSintonizacao(),
                        item.getQuantidade(),
                        item.getTipoItem()
                ))
                .collect(Collectors.toList())
                : new ArrayList<>();

        // ============================================
        // 🔥 CONVERTER PERÍCIAS (COM VERIFICAÇÃO DE NULL)
        // ============================================
        List<PericiaPersonagemDTO> periciasDTO = personagem.getPericiasPersonagem() != null
                ? personagem.getPericiasPersonagem().stream()
                .map(pp -> PericiaPersonagemDTO.from(pp, personagem, bonusProficiencia))
                .collect(Collectors.toList())
                : new ArrayList<>();

        // ============================================
        // 🔥 CONVERTER HABILIDADES (COM VERIFICAÇÃO DE NULL)
        // ============================================
        List<HabilidadeDTO> habilidadesDTO = personagem.getHabilidades() != null
                ? personagem.getHabilidades().stream()
                .map(habilidade -> new HabilidadeDTO(
                        habilidade.getNomeHabilidade(),
                        habilidade.getDescricaoHabilidade(),
                        habilidade.getOrigemHabilidade(),
                        habilidade.getUsosHabilidade(),
                        habilidade.getRecargaHabilidade()
                ))
                .collect(Collectors.toList())
                : new ArrayList<>();

        // ============================================
        // 🔥 CONVERTER PROFICIÊNCIAS (COM VERIFICAÇÃO DE NULL)
        // ============================================
        List<ProficienciaDTO> proficienciaDTO = personagem.getProficienciasPersonagem() != null
                ? personagem.getProficienciasPersonagem().stream()
                .map(proficiencia -> new ProficienciaDTO(
                        proficiencia.getTipoProficiencia(),
                        proficiencia.getListaProficiencias()
                ))
                .collect(Collectors.toList())
                : new ArrayList<>();

        // ============================================
        // 🔥 CONVERTER MAGIAS (COM VERIFICAÇÃO DE NULL)
        // ============================================
        List<MagiaDTO> magiasDTO = personagem.getMagias() != null
                ? personagem.getMagias().stream()
                .map(magia -> new MagiaDTO(
                        magia.getCasting_time(),     // 1 - casting_time
                        magia.getClasses(),         // 2 - classes
                        toComponentsDTO(magia.getComponents()),  // ← MÉTODO AUXILIAR!
                        magia.getDescription(),     // 4 - description
                        magia.getDuration(),        // 5 - duration
                        magia.getLevel(),           // 6 - level
                        magia.getName(),            // 7 - name
                        magia.getRange(),           // 8 - range
                        magia.isRitual(),           // 9 - ritual
                        magia.isConcentration(),    // 10 - concentration
                        magia.getSchool(),          // 11 - school
                        magia.getTags(),            // 12 - tags
                        magia.getType()             // 13 - type
                ))
                .collect(Collectors.toList())
                : new ArrayList<>();

        // ============================================
        // LOG PARA DEBUG
        // ============================================
        System.out.println("📊 RESPOSTA - Habilidades: " + habilidadesDTO.size());
        System.out.println("📊 RESPOSTA - Magias: " + magiasDTO.size());
        System.out.println("📊 RESPOSTA - Itens: " + inventarioDTO.size());
        System.out.println("📊 RESPOSTA - Proficiências: " + proficienciaDTO.size());

        // ============================================
        // RETORNAR DTO
        // ============================================
        return new PersonagemResponseDTO(
                personagem.getId(),
                personagem.getNomePersonagem(),
                personagem.getNivelPersonagem(),
                new ClasseInfo(
                        classe.getNomeClasse(),
                        classe.getDadoDeVida(),
                        classe.isConjurador(),
                        classe.getAtributoConjuracao(),
                        classe.getTipoConjuracao(),
                        classe.getProficienciaSalvaguarda()
                ),
                new AtributosInfo(
                        personagem.getValorForca(),
                        personagem.getValorDestreza(),
                        personagem.getValorConstituicao(),
                        personagem.getValorInteligencia(),
                        personagem.getValorSabedoria(),
                        personagem.getValorCarisma()
                ),
                new MagiaInfo(bonusProficiencia, cd, ataque),
                inventarioDTO,
                periciasDTO,
                habilidadesDTO,
                proficienciaDTO,
                magiasDTO,  // ← ADICIONADO!
                personagem.getHistoriaPersonagem(),
                personagem.getAparenciaPersonagem(),
                personagem.getIdeaisPersonagem(),
                personagem.getDefeitosPersonagem(),
                personagem.getAnotacoesPersonagem(),
                personagem.getPersonalidadePersonagem(),
                personagem.getRacaPersonagem(),
                personagem.getEscalaPersonagem(),
                personagem.getAlinhamentoPersonagem(),
                personagem.getPesoPersonagem(),
                personagem.getAlturaPersonagem(),
                ca,
                iniciativa,
                movimento,
                personagem.getPontosVidaPersonagem()
        );
    }
}