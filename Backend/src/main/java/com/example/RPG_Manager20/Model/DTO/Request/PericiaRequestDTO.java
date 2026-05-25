package com.example.RPG_Manager20.Model.DTO.Request;

import com.example.RPG_Manager20.Model.Enums.Atributos;

public record PericiaRequestDTO (
        String nomePericia,
        Atributos atributoChave,
        int valorTotal
){
}
