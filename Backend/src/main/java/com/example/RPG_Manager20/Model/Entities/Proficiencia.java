package com.example.RPG_Manager20.Model.Entities;

import com.example.RPG_Manager20.Model.Enums.TipoProficiencia;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_proficiencia")
//evitar confusões futuras, isso serve para idiomas,
//outras proficiênias que o personagem venha conseguir futuramente(talentos, multi-classe)
//e também para editar antigas
public class Proficiencia extends AbstractModel{
    private TipoProficiencia tipoProficiencia;
    private String listaProficiencias;

    @ManyToOne
    @JoinColumn(name = "personagem_id")
    @JsonIgnore
    private Personagem personagem;

    public Proficiencia(TipoProficiencia tipoProficiencia, String listaProficiencias, Personagem personagem) {
        this.tipoProficiencia = tipoProficiencia;
        this.listaProficiencias = listaProficiencias;
        this.personagem = personagem;
    }

    public Proficiencia() {
    }

    public TipoProficiencia getTipoProficiencia() {
        return tipoProficiencia;
    }

    public void setTipoProficiencia(TipoProficiencia tipoProficiencia) {
        this.tipoProficiencia = tipoProficiencia;
    }

    public String getListaProficiencias() {
        return listaProficiencias;
    }

    public void setListaProficiencias(String listaProficiencias) {
        this.listaProficiencias = listaProficiencias;
    }

    public Personagem getPersonagem() {
        return personagem;
    }

    public void setPersonagem(Personagem personagem) {
        this.personagem = personagem;
    }
}
