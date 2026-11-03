package com.example.RPG_Manager20.Model.DTO.Request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PericiaPersonagemRequestDTO(
        @NotBlank(message = "Slug da perícia é obrigatório")
        String slug,

        @JsonProperty("proficiente")
        boolean isProficiente
) {}