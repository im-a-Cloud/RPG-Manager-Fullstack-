package com.example.RPG_Manager20.Model.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "tb_usuario")
public class Usuario extends AbstractModel {

    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;            // ← tem que ser "email", não "emailUsuario"

    @NotBlank
    @Column(nullable = false)
    private String senha;            // ← "senha", não "senhaUsuario"

    @NotBlank
    @Column(nullable = false)
    private String nome;             // ← "nome", não "nomeUsuario"

    // getters/setters
    public String getEmailUsuario() { return email; }
    public void setEmailUsuario(String email) { this.email = email; }

    public String getSenhaUsuario() { return senha; }
    public void setSenhaUsuario(String senha) { this.senha = senha; }

    public String getNomeUsuario() { return nome; }
    public void setNomeUsuario(String nome) { this.nome = nome; }
}