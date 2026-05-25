package com.example.RPG_Manager20.Model.Enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Classes {
    BARBARO("barbarian", "BÁRBARO"),
    BARDO("bard", "BARDO"),
    BRUXO("warlock", "BRUXO"),
    CLERIGO("cleric", "CLÉRIGO"),
    DRUIDA("druid", "DRUIDA"),
    FEITICEIRO("sorcerer", "FEITICEIRO"),
    GUERREIRO("fighter", "GUERREIRO"),
    LADINO("rogue", "LADINO"),
    MAGO("wizard", "MAGO"),
    PALADINO("paladin", "PALADINO"),
    RANGER("ranger", "RANGER"),
    ARTIFICIE("artificer", "ARTIFICIE");

    private final String tagJson;
    private final String nomeEnum;

    Classes(String tagJson, String nomeEnum) {
        this.tagJson = tagJson;
        this.nomeEnum = nomeEnum;
    }

    @JsonValue
    public String getTagJson() {
        return tagJson;
    }

    @JsonCreator
    public static Classes fromString(String value) {
        if (value == null) return null;

        for (Classes classe : values()) {
            if (classe.name().equals(value)) {
                return classe;
            }
            if (classe.nomeEnum.equals(value)) {
                return classe;
            }
            if (classe.tagJson.equalsIgnoreCase(value)) {
                return classe;
            }
        }

        throw new IllegalArgumentException("Classe não encontrada: " + value);
    }

    public String getNomePortugues() {
        return nomeEnum;
    }
}