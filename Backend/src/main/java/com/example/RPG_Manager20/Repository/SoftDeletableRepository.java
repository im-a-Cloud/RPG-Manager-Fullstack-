package com.example.RPG_Manager20.Repository;

import com.example.RPG_Manager20.Model.Entities.AbstractModel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

@NoRepositoryBean
public interface SoftDeletableRepository<M extends AbstractModel>
        extends JpaRepository<M, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE #{#entityName} e SET e.deleted = true WHERE e.id = :id")
    void delete(@Param("id") Long id);
}