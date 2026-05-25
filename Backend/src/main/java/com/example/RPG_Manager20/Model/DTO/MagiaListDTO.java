package com.example.RPG_Manager20.Model.DTO;

import java.util.List;

public record MagiaListDTO(
        String name,
        String level,
        String school,
        List<String> classes,
        String casting_time,
        String duration,
        String range,
        boolean ritual
) {}
