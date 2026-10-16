package com.example.RPG_Manager20.Repository;

import com.example.RPG_Manager20.Model.Entities.Personagem;

import java.util.List;

public interface PersonagemRepository extends SoftDeletableRepository<Personagem> {

    List<Personagem> findByUsuarioId(Long usuarioId);

}
