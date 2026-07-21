package com.example.RPG_Manager20.Model.DTO;

import com.example.RPG_Manager20.Model.Enums.Atributos;

public record PericiaDTO(
        Long id,
        String nomePericia,
        Atributos atributoChave
){}
