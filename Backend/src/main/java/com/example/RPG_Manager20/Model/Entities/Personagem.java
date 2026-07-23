package com.example.RPG_Manager20.Model.Entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_personagem")
@Data  // ← ADICIONE (cria getters, setters, toString, equals, hashCode)
@AllArgsConstructor  // ← ADICIONE (construtor com todos os campos)
public class Personagem extends AbstractModel {

    @Column(nullable = false)
    private String nomePersonagem = "Personagem sem nome";
    @Column(nullable = false)
    private int nivelPersonagem = 1;

    // Atributos
    @Column(nullable = false)
    private Integer valorForca = 10;
    @Column(nullable = false)
    private Integer valorDestreza = 10;
    @Column(nullable = false)
    private Integer valorConstituicao = 10;
    @Column(nullable = false)
    private Integer valorInteligencia = 10;
    @Column(nullable = false)
    private Integer valorSabedoria = 10;
    @Column(nullable = false)
    private Integer valorCarisma = 10;

    //Descritivos(não obrigatorios para criar o personagem)
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

    //Semi-importantes(Não tem um BD de raças então é só string normal)
    private String racaPersonagem ="";
    private double pesoPersonagem = 0;
    private double alturaPersonagem = 0;
    @Column(columnDefinition = "TEXT")
    private String escalaPersonagem = "";
    @Column(columnDefinition = "TEXT")
    private String alinhamentoPersonagem = "";

    //CA, iniciativa, movimento, dado de vida, pontos de vida
    @Column(nullable = false)
    private Integer caPersonagem = 10;
    @Column(nullable = false)
    private Integer iniciativaPersonagem = 0;
    @Column(nullable = false)
    private Integer movimentoPersonagem = 9;
    @Column(nullable = false)
    private Integer pontosVidaPersonagem = 1;
    //Ligacção com classe, permitindo que um personagem tenha classe

    @ManyToOne
    @JoinColumn(name = "classe_id")
    @JsonIgnore
    private Classe classePersonagem;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "personagem_id")  // ← FK na tabela habilidade
    private List<Habilidade> habilidadesPersonagem = new ArrayList<>();

    // 3️⃣ 🔥 PROFICIÊNCIAS (OneToMany com CASCADE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "personagem_id")  // ← FK na tabela proficiencia
    private List<Proficiencia> proficienciasPersonagem = new ArrayList<>();

    // 4️⃣ 🔥 ITENS (OneToMany com CASCADE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "personagem_id")  // ← FK na tabela item
    private List<Item> inventarioPersonagem = new ArrayList<>();

    // 5️⃣ 🔥 MAGIAS (OneToMany com CASCADE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "personagem_id")  // ← FK na tabela magia
    private List<Magia> magias = new ArrayList<>();

    //Pericias
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "personagem_id")  // ← FK na tabela magia
    private List<PersonagemPericia> periciasPersonagem = new ArrayList<>();

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

    public void adicionarHabilidade(Habilidade habilidade) {
        this.habilidadesPersonagem.add(habilidade);
    }

    public void removerHabilidade(Long habilidadeId) {
        this.habilidadesPersonagem.removeIf(h -> h.getId().equals(habilidadeId));
    }

    public void adicionarItem(Item item) {
        // Verifica se já existe item igual
        for (Item i : inventarioPersonagem) {
            if (i.getNomeItem().equals(item.getNomeItem())) {
                i.setQuantidade(i.getQuantidade() + item.getQuantidade());
                return;
            }
        }
        this.inventarioPersonagem.add(item);
    }

    public void removerItem(Long itemId) {
        this.inventarioPersonagem.removeIf(i -> i.getId().equals(itemId));
    }

    public void adicionarMagia(Magia magia) {
        this.magias.add(magia);
    }

    public void removerMagia(Long magiaId) {
        this.magias.removeIf(m -> m.getId().equals(magiaId));
    }

    public void adicionarProficiencia(Proficiencia proficiencia) {
        this.proficienciasPersonagem.add(proficiencia);
    }

    public void removerProficiencia(Long proficienciaId) {
        this.proficienciasPersonagem.removeIf(p -> p.getId().equals(proficienciaId));
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

    public List<Proficiencia> getProficienciasPersonagem() {
        return proficienciasPersonagem;
    }

    public void setProficienciasPersonagem(List<Proficiencia> proficienciasPersonagem) {
        this.proficienciasPersonagem = proficienciasPersonagem;
    }
    public Long getId() {
        return super.getId();  // ← CHAMA O GETTER DO AbstractModel
    }
    // Se quiser manter o nome "id" no JSON
    public Long getIdClasse() {
        return super.getId();
    }
}