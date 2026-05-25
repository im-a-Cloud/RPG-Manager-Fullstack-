package com.example.RPG_Manager20.Model.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_personagem_pericia")
public class PersonagemPericia extends AbstractModel{
    @ManyToOne
    @JoinColumn(name = "personagem_id")
    private Personagem personagem;

    @ManyToOne
    @JoinColumn(name = "pericia_id")
    private Pericia pericia;

    private boolean isProficiente;

    public PersonagemPericia() {
    }

    public PersonagemPericia(Personagem personagem, Pericia pericia, boolean isProficiente) {
        this.personagem = personagem;
        this.pericia = pericia;
        this.isProficiente = isProficiente;
    }

    public Personagem getPersonagem() {
        return personagem;
    }

    public void setPersonagem(Personagem personagem) {
        this.personagem = personagem;
    }

    public Pericia getPericia() {
        return pericia;
    }

    public void setPericia(Pericia pericia) {
        this.pericia = pericia;
    }

    public boolean isProficiente() {
        return isProficiente;
    }

    public void setProficiente(boolean proficiente) {
        isProficiente = proficiente;
    }
}

