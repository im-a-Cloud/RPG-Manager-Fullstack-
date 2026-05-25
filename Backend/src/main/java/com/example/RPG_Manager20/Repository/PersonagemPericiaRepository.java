package com.example.RPG_Manager20.Repository;

import com.example.RPG_Manager20.Model.Entities.PersonagemPericia;

import java.util.List;
import java.util.Optional;

public interface PersonagemPericiaRepository extends SoftDeletableRepository<PersonagemPericia> {
    List<PersonagemPericia> findByPersonagemId(Long personagemId);

    // Buscar uma perícia específica de um personagem
    Optional<PersonagemPericia> findByPersonagemIdAndPericiaId(Long personagemId, Long periciaId);

    // Verificar se existe
    boolean existsByPersonagemIdAndPericiaId(Long personagemId, Long periciaId);

    // Deletar uma perícia específica
    void deleteByPersonagemIdAndPericiaId(Long personagemId, Long periciaId);

}
