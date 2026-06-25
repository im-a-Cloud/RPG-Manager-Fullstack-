package com.example.RPG_Manager20.Model.Mapper;

import com.example.RPG_Manager20.Model.DTO.ProficienciaDTO;
import com.example.RPG_Manager20.Model.Entities.Proficiencia;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface ProficienciaMapper {
    Proficiencia toEntity(ProficienciaDTO proficienciaDTO);
    ProficienciaDTO toDto(Proficiencia entity);
}
