package com.example.RPG_Manager20.Model.Entities;

import com.example.RPG_Manager20.Model.Enums.Atributos;
import com.example.RPG_Manager20.Model.Enums.Classes;
import com.example.RPG_Manager20.Model.Enums.TipoConjuracao;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name= "tb_classes")
public class Classe extends AbstractModel{
    private Classes nomeClasse;
    private int dadoDeVida;
    private List<String> proficienciasArmas;
    private List<String> proficienciasArmaduras;
    private List<Atributos> proficienciaSalvaguarda;
    private List<String> proficienciaFerramentas;

    private boolean isConjurador;

    @Enumerated(EnumType.STRING)
    private Atributos atributoConjuracao;

    private TipoConjuracao tipoConjuracao;

    public Classe() {
    }

    public Classe(Classes nomeClasse, int dadoDeVida, List<String> proficienciasArmas, List<String> proficienciasArmaduras, List<Atributos> proficienciaSalvaguarda, List<String> proficienciaFerramentas, boolean isConjurador, Atributos atributoConjuracao, TipoConjuracao tipoConjuracao) {
        this.nomeClasse = nomeClasse;
        this.dadoDeVida = dadoDeVida;
        this.proficienciasArmas = proficienciasArmas;
        this.proficienciasArmaduras = proficienciasArmaduras;
        this.proficienciaSalvaguarda = proficienciaSalvaguarda;
        this.proficienciaFerramentas = proficienciaFerramentas;
        this.isConjurador = isConjurador;
        this.atributoConjuracao = atributoConjuracao;
        this.tipoConjuracao = tipoConjuracao;
    }

    public Classes getNomeClasse() {
        return nomeClasse;
    }

    public void setNomeClasse(Classes nomeClasse) {
        this.nomeClasse = nomeClasse;
    }

    public int getDadoDeVida() {
        return dadoDeVida;
    }

    public void setDadoDeVida(int dadoDeVida) {
        this.dadoDeVida = dadoDeVida;
    }

    public List<String> getProficienciasArmas() {
        return proficienciasArmas;
    }

    public void setProficienciasArmas(List<String> proficienciasArmas) {
        this.proficienciasArmas = proficienciasArmas;
    }

    public List<String> getProficienciasArmaduras() {
        return proficienciasArmaduras;
    }

    public void setProficienciasArmaduras(List<String> proficienciasArmaduras) {
        this.proficienciasArmaduras = proficienciasArmaduras;
    }

    public List<Atributos> getProficienciaSalvaguarda() {
        return proficienciaSalvaguarda;
    }

    public void setProficienciaSalvaguarda(List<Atributos> proficienciaSalvaguarda) {
        this.proficienciaSalvaguarda = proficienciaSalvaguarda;
    }

    public List<String> getProficienciaFerramentas() {
        return proficienciaFerramentas;
    }

    public void setProficienciaFerramentas(List<String> proficienciaFerramentas) {
        this.proficienciaFerramentas = proficienciaFerramentas;
    }

    public boolean isConjurador() {
        return isConjurador;
    }

    public void setConjurador(boolean conjurador) {
        isConjurador = conjurador;
    }

    public Atributos getAtributoConjuracao() {
        return atributoConjuracao;
    }

    public void setAtributoConjuracao(Atributos atributoConjuracao) {
        this.atributoConjuracao = atributoConjuracao;
    }

    public TipoConjuracao getTipoConjuracao() {
        return tipoConjuracao;
    }

    public void setTipoConjuracao(TipoConjuracao tipoConjuracao) {
        this.tipoConjuracao = tipoConjuracao;
    }
}
