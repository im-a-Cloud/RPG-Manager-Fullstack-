package com.example.RPG_Manager20.Model.Entities;

import com.example.RPG_Manager20.Model.Enums.Atributos;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_personagem")
public class Personagem extends AbstractModel {

    @Column(nullable = false)
    private String nomePersonagem = "Personagem sem nome";
    @Column(nullable = false)
    private int nivelPersonagem = 1;

    // Atributos
    @Column(nullable = false)
    private int valorForca = 10;
    @Column(nullable = false)
    private int valorDestreza = 10;
    @Column(nullable = false)
    private int valorConstituicao = 10;
    @Column(nullable = false)
    private int valorInteligencia = 10;
    @Column(nullable = false)
    private int valorSabedoria = 10;
    @Column(nullable = false)
    private int valorCarisma = 10;

    //Descritivos(não obrigatorios para criar o personagem, inicialmente)
    @Column(columnDefinition = "TEXT")
    private String historiaPersonagem ="";
    @Column(columnDefinition = "TEXT")
    private String aparenciaPersonagem ="";
    @Column(columnDefinition = "TEXT")
    private String ideaisPersonagem ="";
    @Column(columnDefinition = "TEXT")
    private String defeitosPersonagem ="";
    @Column(columnDefinition = "TEXT")
    private String anotacoesPersonagem ="";
    @Column(columnDefinition = "TEXT")
    private String personalidadePersonagem ="";
    @Column(columnDefinition = "TEXT")
    private String racaPersonagem ="";
    private double pesoPersonagem = 0;
    private double alturaPersonagem = 0;
    @Column(columnDefinition = "TEXT")
    private String escalaPersonagem = "";
    @Column(columnDefinition = "TEXT")
    private String alinhamentoPersonagem = "";

    //CA, iniciativa, movimento, dado de vida, pontos de vida
    @Column(nullable = false)
    private int caPersonagem = 10;
    @Column(nullable = false)
    private int iniciativaPersonagem = this.getValorDestreza();
    @Column(nullable = false)
    private int movimentoPersonagem = 9;
    @Column(nullable = false)
    private int pontosVidaPersonagem = 10;

    //Ligacção com classe, permitindo que um personagem tenha classe

    @ManyToOne
    @JoinColumn(name = "classe_id")
    private Classe classePersonagem;

    @ManyToMany
    @JoinTable(
            name = "personagem_magia",
            joinColumns = @JoinColumn(name = "personagem_id"),
            inverseJoinColumns = @JoinColumn(name = "magia_id")
    )
    private List<Magia> magias = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "personagem_item",
            joinColumns = @JoinColumn(name = "personagem_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<Item> inventarioPersonagem = new ArrayList<>();

    @OneToMany(mappedBy = "personagem")
    private List<PersonagemPericia> periciasPersonagem = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "personagem_habilidade",
            joinColumns = @JoinColumn(name = "personagem_id"),
            inverseJoinColumns = @JoinColumn(name = "habilidade_id")
    )
    private List<Habilidade> habilidadesPersonagem = new ArrayList<>();

    //ligacão com proficiencias
    @ManyToMany
    @JoinTable(
            name = "personagem_proficiencia",
            joinColumns = @JoinColumn(name = "personagem_id"),
            inverseJoinColumns = @JoinColumn(name ="proficiencia_id")
    )
    private List<Proficiencia> proficienciasPersonagem = new ArrayList<>();


    public Personagem() {
    }

    public Personagem(String nomePersonagem, int nivelPersonagem, int valorForca, int valorDestreza, int valorConstituicao, int valorInteligencia, int valorSabedoria, int valorCarisma, Classe classePersonagem, List<Magia> magias, List<Item> inventarioPersonagem, List<PersonagemPericia> periciasPersonagem, List<Habilidade> habilidadesPersonagem) {
        this.nomePersonagem = nomePersonagem;
        this.nivelPersonagem = nivelPersonagem;
        this.valorForca = valorForca;
        this.valorDestreza = valorDestreza;
        this.valorConstituicao = valorConstituicao;
        this.valorInteligencia = valorInteligencia;
        this.valorSabedoria = valorSabedoria;
        this.valorCarisma = valorCarisma;
        this.classePersonagem = classePersonagem;
        this.magias = magias;
        this.inventarioPersonagem = inventarioPersonagem;
        this.periciasPersonagem = periciasPersonagem;
        this.habilidadesPersonagem = habilidadesPersonagem;
    }

    public List<Habilidade> getHabilidadesPersonagem() {
        return habilidadesPersonagem;
    }

    public void setHabilidadesPersonagem(List<Habilidade> habilidadesPersonagem) {
        this.habilidadesPersonagem = habilidadesPersonagem;
    }

    public List<PersonagemPericia> getPericiasPersonagem() {
        return periciasPersonagem;
    }

    public void setPericiasPersonagem(List<PersonagemPericia> periciasPersonagem) {
        this.periciasPersonagem = periciasPersonagem;
    }

    public List<Magia> getMagias() {
        return magias;
    }

    public void setMagias(List<Magia> magias) {
        this.magias = magias;
    }

    public void adicionarMagia(Magia magia) {
        this.magias.add(magia);
    }

    public void removerMagia(Magia magia) {
        this.magias.remove(magia);
    }

    public void adicionarItem(Item item) {
        for (Item i: inventarioPersonagem){
            if (i.getId().equals(item.getId())){
                i.setQuantidade(i.getQuantidade() + item.getQuantidade());
                return;
            }
        }
        inventarioPersonagem.add(item);
    }

    public void removerItem(Item item) {
        inventarioPersonagem.remove(item);
    }

    public List<Item> getInventarioPersonagem() {
        return inventarioPersonagem;
    }

    public void setInventarioPersonagem(List<Item> inventarioPersonagem) {
        this.inventarioPersonagem = inventarioPersonagem;
    }

    public Classe getClassePersonagem() {
        return classePersonagem;
    }

    public void setClassePersonagem(Classe classePersonagem) {
        this.classePersonagem = classePersonagem;
    }

    public String getNomePersonagem() {
        return nomePersonagem;
    }

    public void setNomePersonagem(String nomePersonagem) {
        this.nomePersonagem = nomePersonagem;
    }

    public int getNivelPersonagem() {
        return nivelPersonagem;
    }

    public void setNivelPersonagem(int nivelPersonagem) {
        this.nivelPersonagem = nivelPersonagem;
    }

    public int getValorForca() {
        return valorForca;
    }

    public void setValorForca(int valorForca) {
        this.valorForca = valorForca;
    }

    public int getValorDestreza() {
        return valorDestreza;
    }

    public void setValorDestreza(int valorDestreza) {
        this.valorDestreza = valorDestreza;
    }

    public int getValorConstituicao() {
        return valorConstituicao;
    }

    public void setValorConstituicao(int valorConstituicao) {
        this.valorConstituicao = valorConstituicao;
    }

    public int getValorInteligencia() {
        return valorInteligencia;
    }

    public void setValorInteligencia(int valorInteligencia) {
        this.valorInteligencia = valorInteligencia;
    }

    public int getValorSabedoria() {
        return valorSabedoria;
    }

    public void setValorSabedoria(int valorSabedoria) {
        this.valorSabedoria = valorSabedoria;
    }

    public int getValorCarisma() {
        return valorCarisma;
    }

    public void setValorCarisma(int valorCarisma) {
        this.valorCarisma = valorCarisma;
    }


    public String getHistoriaPersonagem() {
        return historiaPersonagem;
    }

    public void setHistoriaPersonagem(String historiaPersonagem) {
        this.historiaPersonagem = historiaPersonagem;
    }

    public String getAparenciaPersonagem() {
        return aparenciaPersonagem;
    }

    public void setAparenciaPersonagem(String aparenciaPersonagem) {
        this.aparenciaPersonagem = aparenciaPersonagem;
    }

    public String getIdeaisPersonagem() {
        return ideaisPersonagem;
    }

    public void setIdeaisPersonagem(String ideaisPersonagem) {
        this.ideaisPersonagem = ideaisPersonagem;
    }

    public String getDefeitosPersonagem() {
        return defeitosPersonagem;
    }

    public void setDefeitosPersonagem(String defeitosPersonagem) {
        this.defeitosPersonagem = defeitosPersonagem;
    }

    public String getAnotacoesPersonagem() {
        return anotacoesPersonagem;
    }

    public void setAnotacoesPersonagem(String anotacoesPersonagem) {
        this.anotacoesPersonagem = anotacoesPersonagem;
    }

    public String getPersonalidadePersonagem() {
        return personalidadePersonagem;
    }

    public void setPersonalidadePersonagem(String personalidadePersonagem) {
        this.personalidadePersonagem = personalidadePersonagem;
    }

    public String getRacaPersonagem() {
        return racaPersonagem;
    }

    public void setRacaPersonagem(String racaPersonagem) {
        this.racaPersonagem = racaPersonagem;
    }

    public double getPesoPersonagem() {
        return pesoPersonagem;
    }

    public void setPesoPersonagem(double pesoPersonagem) {
        this.pesoPersonagem = pesoPersonagem;
    }

    public double getAlturaPersonagem() {
        return alturaPersonagem;
    }

    public void setAlturaPersonagem(double alturaPersonagem) {
        this.alturaPersonagem = alturaPersonagem;
    }

    public String getEscalaPersonagem() {
        return escalaPersonagem;
    }

    public void setEscalaPersonagem(String escalaPersonagem) {
        this.escalaPersonagem = escalaPersonagem;
    }

    public String getAlinhamentoPersonagem() {
        return alinhamentoPersonagem;
    }

    public void setAlinhamentoPersonagem(String alinhamentoPersonagem) {
        this.alinhamentoPersonagem = alinhamentoPersonagem;
    }

    public int getCaPersonagem() {
        return caPersonagem;
    }

    public void setCaPersonagem(int caPersonagem) {
        this.caPersonagem = caPersonagem;
    }

    public int getIniciativaPersonagem() {
        return iniciativaPersonagem;
    }

    public void setIniciativaPersonagem(int iniciativaPersonagem) {
        this.iniciativaPersonagem = iniciativaPersonagem;
    }

    public int getMovimentoPersonagem() {
        return movimentoPersonagem;
    }

    public void setMovimentoPersonagem(int movimentoPersonagem) {
        this.movimentoPersonagem = movimentoPersonagem;
    }

    public int getQuantidadeDadosVida() {
        return this.nivelPersonagem;
    }
    public int getPontosVidaPersonagem() {
        return pontosVidaPersonagem;
    }

    public void setPontosVidaPersonagem(int pontosVidaPersonagem) {
        this.pontosVidaPersonagem = pontosVidaPersonagem;
    }
}