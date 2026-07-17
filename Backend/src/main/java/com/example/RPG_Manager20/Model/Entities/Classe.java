package com.example.RPG_Manager20.Model.Entities;

import com.example.RPG_Manager20.Model.Enums.Atributos;
import com.example.RPG_Manager20.Model.Enums.Classes;
import com.example.RPG_Manager20.Model.Enums.DadosDeVida;
import com.example.RPG_Manager20.Model.Enums.TipoConjuracao;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name= "tb_classes")
public class Classe extends AbstractModel{
    private Classes nomeClasse;
    private DadosDeVida dadoDeVida;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "classe_id")  // ← ADICIONE ESTA ANOTAÇÃO
    private List<Proficiencia> listaProficienciasClasse;
    @ElementCollection
    @CollectionTable(name = "classe_salvaguardas", joinColumns = @JoinColumn(name = "classe_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "atributo")
    private List<Atributos> proficienciaSalvaguarda;

    private boolean isConjurador;

    @Enumerated(EnumType.STRING)
    private Atributos atributoConjuracao;

    private TipoConjuracao tipoConjuracao;

    public Classe(Classes nomeClasse, DadosDeVida dadoDeVida, List<Proficiencia> listaProficienciasClasse,
                  List<Atributos> proficienciaSalvaguarda, boolean isConjurador,
                  Atributos atributoConjuracao, TipoConjuracao tipoConjuracao) {
        this.nomeClasse = nomeClasse;
        this.dadoDeVida = dadoDeVida;
        this.listaProficienciasClasse = listaProficienciasClasse != null ? listaProficienciasClasse : new ArrayList<>();
        this.proficienciaSalvaguarda = proficienciaSalvaguarda != null ? proficienciaSalvaguarda : new ArrayList<>();
        this.isConjurador = isConjurador;
        this.atributoConjuracao = atributoConjuracao;
        this.tipoConjuracao = tipoConjuracao;
    }


    public Classe() {
    }

    public Classes getNomeClasse() {
        return nomeClasse;
    }

    public void setNomeClasse(Classes nomeClasse) {
        this.nomeClasse = nomeClasse;
    }

    public DadosDeVida getDadoDeVida() {
        return dadoDeVida;
    }

    public void setDadoDeVida(DadosDeVida dadoDeVida) {
        this.dadoDeVida = dadoDeVida;
    }

    public List<Proficiencia> getListaProficienciasClasse() {
        return listaProficienciasClasse;
    }

    public void setListaProficienciasClasse(List<Proficiencia> listaProficienciasClasse) {
        this.listaProficienciasClasse = listaProficienciasClasse;
    }

    public List<Atributos> getProficienciaSalvaguarda() {
        return proficienciaSalvaguarda;
    }

    public void setProficienciaSalvaguarda(List<Atributos> proficienciaSalvaguarda) {
        this.proficienciaSalvaguarda = proficienciaSalvaguarda;
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
