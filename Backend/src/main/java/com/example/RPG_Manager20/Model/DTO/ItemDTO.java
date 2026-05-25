package com.example.RPG_Manager20.Model.DTO;

import com.example.RPG_Manager20.Model.Enums.RaridadeItem;

public record ItemDTO (String nomeItem,
                       String descricaoItem,
                       double precoItem,
                       RaridadeItem raridadeItem,
                       double pesoItem,
                       boolean isMagico,
                       boolean precisaSintonizacao,
                       int quantidade) {
}
