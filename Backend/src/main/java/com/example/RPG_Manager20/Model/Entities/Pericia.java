package com.example.RPG_Manager20.Model.Entities;

import com.example.RPG_Manager20.Model.Enums.Atributos;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_pericia")
public class Pericia extends AbstractModel{
    private String nomePericia;
    @Enumerated(EnumType.STRING)
    private Atributos atributoChave;
    private int valorTotal;

    public Pericia() {
    }

    public Pericia(String nomePericia, Atributos atributoChave, int valorTotal) {
        this.nomePericia = nomePericia;
        this.atributoChave = atributoChave;
        this.valorTotal = valorTotal;
    }

    public String getNomePericia() {
        return nomePericia;
    }

    public void setNomePericia(String nomePericia) {
        this.nomePericia = nomePericia;
    }

    public Atributos getAtributoChave() {
        return atributoChave;
    }

    public void setAtributoChave(Atributos atributoChave) {
        this.atributoChave = atributoChave;
    }

    public int getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(int valorTotal) {
        this.valorTotal = valorTotal;
    }
}
