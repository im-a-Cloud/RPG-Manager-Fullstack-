package com.example.RPG_Manager20.Repository;

import com.example.RPG_Manager20.Model.Entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends SoftDeletableRepository<Usuario> {

    Optional<Usuario> findByEmail(String emailUsuario);

    boolean existsByEmail(String emailUsuario);
}
