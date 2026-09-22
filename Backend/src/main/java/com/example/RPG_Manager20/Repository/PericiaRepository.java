package com.example.RPG_Manager20.Repository;

import com.example.RPG_Manager20.Model.Entities.Pericia;

import java.util.List;
import java.util.Optional;

public interface PericiaRepository extends SoftDeletableRepository<Pericia> {

    Optional<Pericia> findBySlug(String slug);

    boolean existsBySlug(String slug);

    List<Pericia> findAllByOrderByNomeExibicaoAsc();
}
