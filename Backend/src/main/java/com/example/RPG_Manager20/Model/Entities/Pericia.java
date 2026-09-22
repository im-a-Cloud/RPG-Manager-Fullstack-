package com.example.RPG_Manager20.Model.Entities;

import com.example.RPG_Manager20.Model.Enums.Atributos;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "tb_pericia")
public class Pericia extends AbstractModel {

    @NotBlank
    @Column(nullable = false, unique = true, length = 50)
    private String slug;                 // ex: "atuacao"

    @NotBlank
    @Column(name = "nome_exibicao", nullable = false, length = 100)
    private String nomeExibicao;         // ex: "Atuação"

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "atributo_chave", nullable = false, length = 20)
    private Atributos atributoChave;

    private int valorTotal;

    // ============ getters/setters ============
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getNomeExibicao() { return nomeExibicao; }
    public void setNomeExibicao(String nomeExibicao) { this.nomeExibicao = nomeExibicao; }

    public Atributos getAtributoChave() { return atributoChave; }
    public void setAtributoChave(Atributos atributoChave) { this.atributoChave = atributoChave; }

    public int getValorTotal() { return valorTotal; }
    public void setValorTotal(int valorTotal) { this.valorTotal = valorTotal; }
}