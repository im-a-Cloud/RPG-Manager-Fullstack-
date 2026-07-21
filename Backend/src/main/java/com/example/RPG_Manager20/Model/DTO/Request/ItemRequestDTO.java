package com.example.RPG_Manager20.Model.DTO.Request;

import com.example.RPG_Manager20.Model.Enums.RaridadeItem;
import com.example.RPG_Manager20.Model.Enums.TipoItem;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ItemRequestDTO (
        @NotBlank(message = "Nome é obrigatório")
        String nomeItem,

        @NotNull(message = "Tipo é obrigatório")
        TipoItem tipoItem,

        String descricaoItem,

        @NotNull(message = "Preço é obrigatório")
        @Min(value = 0, message = "Preço não pode ser negativo")
        Double precoItem,

        @NotNull(message = "Raridade é obrigatória")
        RaridadeItem raridadeItem,

        @NotNull(message = "Peso é obrigatório")
        @Min(value = 0, message = "Peso não pode ser negativo")
        Double pesoItem,

        Boolean isMagico,
        Boolean precisaSintonizacao,

        @Min(value = 1, message = "Quantidade mínima 1")
        int quantidade
) {
    public ItemRequestDTO{
        if(nomeItem == null)nomeItem = "";
        if(tipoItem == null) tipoItem = TipoItem.ARMA;
        if(descricaoItem == null) descricaoItem = "";
        if(precoItem == null) precoItem = 0.0;
        if(pesoItem == null) pesoItem = 0.0;
        if(quantidade <= 0) quantidade = 1;
        if (isMagico == null) isMagico = false;
        if (precisaSintonizacao == null) precisaSintonizacao = false;
    }
}
