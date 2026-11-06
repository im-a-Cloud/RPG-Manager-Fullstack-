package com.example.RPG_Manager20.Model.Enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Classes {
    BARBARO("barbarian", "Bárbaro", DadosDeVida.D12, false, null, TipoConjuracao.NENHUM),
    BARDO("bard", "Bardo", DadosDeVida.D8, true, Atributos.CARISMA, TipoConjuracao.COMPLETO),
    BRUXO("warlock", "Bruxo", DadosDeVida.D8, true, Atributos.CARISMA, TipoConjuracao.COMPLETO),
    CLERIGO("cleric", "Clérigo", DadosDeVida.D8, true, Atributos.SABEDORIA, TipoConjuracao.COMPLETO),
    DRUIDA("druid", "Druida", DadosDeVida.D8, true, Atributos.SABEDORIA, TipoConjuracao.COMPLETO),
    FEITICEIRO("sorcerer", "Feiticeiro", DadosDeVida.D6, true, Atributos.CARISMA, TipoConjuracao.COMPLETO),
    GUERREIRO("fighter", "Guerreiro", DadosDeVida.D10, false, null, TipoConjuracao.NENHUM),
    LADINO("rogue", "Ladino", DadosDeVida.D8, false, null, TipoConjuracao.NENHUM),
    MAGO("wizard", "Mago", DadosDeVida.D6, true, Atributos.INTELIGENCIA, TipoConjuracao.COMPLETO),
    MONGE("monk", "Monge", DadosDeVida.D8, false, null, TipoConjuracao.NENHUM),
    PALADINO("paladin", "Paladino", DadosDeVida.D10, true, Atributos.CARISMA, TipoConjuracao.MEIO),
    PATRULHEIRO("ranger", "Patrulheiro", DadosDeVida.D10, true, Atributos.SABEDORIA, TipoConjuracao.MEIO),
    ARTIFICIE("artificer", "Artificie", DadosDeVida.D8, true, Atributos.INTELIGENCIA, TipoConjuracao.MEIO);

    private final String tagJson;
    private final String nomePortugues;
    private final DadosDeVida dadoDeVida;
    private final boolean isConjurador;
    private final Atributos atributoConjuracao;
    private final TipoConjuracao tipoConjuracao;

    Classes(String tagJson, String nomePortugues, DadosDeVida dadoDeVida,
            boolean isConjurador, Atributos atributoConjuracao, TipoConjuracao tipoConjuracao) {
        this.tagJson = tagJson;
        this.nomePortugues = nomePortugues;
        this.dadoDeVida = dadoDeVida;
        this.isConjurador = isConjurador;
        this.atributoConjuracao = atributoConjuracao;
        this.tipoConjuracao = tipoConjuracao;
    }

    // ─── @JsonValue: o que sai no JSON ───
    // Escolha UM: tagJson ("fighter") ou nomePortugues ("Guerreiro").
    // Para o front mostrar PT, use nomePortugues.
    @JsonValue
    public String getNomePortugues() {
        return nomePortugues;
    }

    // ─── @JsonCreator: o que entra do JSON ───
    // Aceita os três formatos (name, tagJson, nomePortugues) para compatibilidade.
    @JsonCreator
    public static Classes fromString(String value) {
        if (value == null) return null;
        for (Classes classe : values()) {
            if (classe.name().equalsIgnoreCase(value)
                    || classe.tagJson.equalsIgnoreCase(value)
                    || classe.nomePortugues.equalsIgnoreCase(value)) {
                return classe;
            }
        }
        throw new IllegalArgumentException("Classe não encontrada: " + value);
    }

    // ─── Getters auxiliares (não vão para o JSON) ───
    public String getTagJson() { return tagJson; }
    public DadosDeVida getDadoDeVida() { return dadoDeVida; }
    public boolean isConjurador() { return isConjurador; }
    public Atributos getAtributoConjuracao() { return atributoConjuracao; }
    public TipoConjuracao getTipoConjuracao() { return tipoConjuracao; }

    // Alias para o front ler "nomeExibicao" também, se quiser
    public String getNomeExibicao() { return nomePortugues; }
}