package com.example.RPG_Manager20.Model.DTO;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record MagiaDTO(
        String casting_time,
        List<String> classes,
        ComponentsDTO components,  // ← Certo, usando ComponentsDTO
        String description,
        String duration,
        @Pattern(regexp = "^(0|1|2|3|4|5|6|7|8|9|cantrip|truque)$",
                message = "Nível deve ser 0-9, cantrip ou truque")
        String level,
        String name,
        String range,
        boolean ritual,
        String school,
        List<String> tags,
        String type
) {}

