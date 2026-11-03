package com.example.RPG_Manager20.Model.DTO.Response;

import com.example.RPG_Manager20.Model.Entities.Item;
import com.example.RPG_Manager20.Model.Enums.RaridadeItem;
import com.example.RPG_Manager20.Model.Enums.TipoItem;

public record ItemResponseDTO(
        Long id,
        String nomeItem,
        TipoItem tipoItem,
        String descricaoItem,
        double precoItem,
        RaridadeItem raridadeItem,
        boolean isMagico,
        boolean precisaSintonizacao,
        int quantidade
) {
    public static ItemResponseDTO from(Item i) {
        if (i == null) return null;
        return new ItemResponseDTO(
                i.getId(),
                i.getNomeItem(),
                i.getTipoItem(),                                    // enum, não .name()
                i.getDescricaoItem(),
                i.getPrecoItem(),  // double
                i.getRaridadeItem(),                                // enum, não .name()
                i.isMagico(),                                       // ← não esqueça
                i.isPrecisaSintonizacao(), // ← não esqueça
                i.getQuantidade()  // int
        );
    }
}