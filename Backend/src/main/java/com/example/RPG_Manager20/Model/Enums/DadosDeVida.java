package com.example.RPG_Manager20.Model.Enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DadosDeVida {
    D6(6),
    D8(8),
    D10(10),
    D12(12);

    private final int valor;

    DadosDeVida(int valor) {
        this.valor = valor;
    }

    @JsonValue
    public int getValor() {
        return valor;
    }

    @JsonCreator
    public static DadosDeVida fromValue(int value) {
        for (DadosDeVida d : values()) {
            if (d.valor == value) {
                return d;
            }
        }
        // Fallback: retorna D6 se o valor não for encontrado
        return D6;
    }

    // 🔥 MÉTODO AUXILIAR PARA CONVERTER DE STRING
    @JsonCreator
    public static DadosDeVida fromString(String value) {
        if (value == null) return D6;

        // Tenta converter por nome (D4, D6, etc.)
        try {
            return DadosDeVida.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Se não funcionar, tenta extrair o número
            try {
                int numero = Integer.parseInt(value.replace("D", ""));
                return fromValue(numero);
            } catch (NumberFormatException ex) {
                return D6;
            }
        }
    }
}