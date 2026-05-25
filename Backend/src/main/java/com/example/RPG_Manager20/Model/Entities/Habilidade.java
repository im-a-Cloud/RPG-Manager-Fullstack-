package com.example.RPG_Manager20.Model.Entities;

import com.example.RPG_Manager20.Model.Enums.OrigemHabilidade;
import jakarta.persistence.*;

@Entity
@Table(name= "tb_habilidades")
public class Habilidade extends AbstractModel{
    private String nomeHabilidade;
    private String descricaoHabilidade;
    @Enumerated(EnumType.STRING)
    @Column(name = "origem_habilidade")
    private OrigemHabilidade origemHabilidade;
    private String usosHabilidade;
    private String recargaHabilidade;

    public Habilidade() {
    }

    public Habilidade(String nomeHabilidade, String descricaoHabilidade, OrigemHabilidade origemHabilidade, String usosHabilidade, String recargaHabilidade) {
        this.nomeHabilidade = nomeHabilidade;
        this.descricaoHabilidade = descricaoHabilidade;
        this.origemHabilidade = origemHabilidade;
        this.usosHabilidade = usosHabilidade;
        this.recargaHabilidade = recargaHabilidade;
    }

    public String getNomeHabilidade() {
        return nomeHabilidade;
    }

    public void setNomeHabilidade(String nomeHabilidade) {
        this.nomeHabilidade = nomeHabilidade;
    }

    public String getDescricaoHabilidade() {
        return descricaoHabilidade;
    }

    public void setDescricaoHabilidade(String descricaoHabilidade) {
        this.descricaoHabilidade = descricaoHabilidade;
    }

    public OrigemHabilidade getOrigemHabilidade() {
        return origemHabilidade;
    }

    public void setOrigemHabilidade(OrigemHabilidade origemHabilidade) {
        this.origemHabilidade = origemHabilidade;
    }

    public String getUsosHabilidade() {
        return usosHabilidade;
    }

    public void setUsosHabilidade(String usosHabilidade) {
        this.usosHabilidade = usosHabilidade;
    }

    public String getRecargaHabilidade() {
        return recargaHabilidade;
    }

    public void setRecargaHabilidade(String recargaHabilidade) {
        this.recargaHabilidade = recargaHabilidade;
    }
}
