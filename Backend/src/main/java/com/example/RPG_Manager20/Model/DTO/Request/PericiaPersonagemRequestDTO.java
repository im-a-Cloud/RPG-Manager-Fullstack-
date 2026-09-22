package com.example.RPG_Manager20.Model.DTO.Request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PericiaPersonagemRequestDTO(
        @JsonProperty("slug") String slug,
        @JsonProperty("isProficiente") Boolean isProficiente
) {
    public PericiaPersonagemRequestDTO {
        if (isProficiente == null) isProficiente = false;
    }
}