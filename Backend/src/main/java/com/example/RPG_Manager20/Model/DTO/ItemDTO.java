package com.example.RPG_Manager20.Model.DTO;

import com.example.RPG_Manager20.Model.Enums.RaridadeItem;
import com.example.RPG_Manager20.Model.Enums.TipoItem;

public record ItemDTO (
        String nomeItem,
        String descricaoItem,
        Double  precoItem,
        RaridadeItem raridadeItem,
        Double  pesoItem,
        Boolean  isMagico,
        Boolean  precisaSintonizacao,
        Integer quantidade,
        TipoItem tipoItem
) {
    public ItemDTO {
        if (nomeItem == null) nomeItem = "";
        if (descricaoItem == null) descricaoItem = "";
        if (precoItem == null) precoItem = 0.0;
        if (raridadeItem == null) raridadeItem = raridadeItem.COMUM;
        if (pesoItem == null) pesoItem = 0.0;
        if (isMagico == null) isMagico = false;
        if (precisaSintonizacao == null) precisaSintonizacao = false;
        if (quantidade == null) quantidade = 1;
        if (tipoItem == null) tipoItem = tipoItem.ARMA;
    }
}
