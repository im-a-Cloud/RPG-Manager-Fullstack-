package com.example.RPG_Manager20.Model.DTO.Response;

import com.example.RPG_Manager20.Model.Enums.RaridadeItem;
import com.example.RPG_Manager20.Model.Enums.TipoItem;

public record ItemResponseDTO (
        Long id,
        String nomeItem,
        TipoItem tipoItem,
        String descricaoItem,
        double precoItem,
        RaridadeItem raridadeItem,
        double pesoItem,
        boolean isMagico,
        boolean precisaSintonizacao,
        int quantidade
)
{}
