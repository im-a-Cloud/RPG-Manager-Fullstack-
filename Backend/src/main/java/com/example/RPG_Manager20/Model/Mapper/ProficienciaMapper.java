package com.example.RPG_Manager20.Model.Mapper;

import com.example.RPG_Manager20.Model.DTO.ProficienciaDTO;
import com.example.RPG_Manager20.Model.Entities.Proficiencia;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")

public interface ProficienciaMapper {

    @Mapping(target = "id", ignore = true)
    Proficiencia toEntity(ProficienciaDTO proficienciaDTO);
    ProficienciaDTO toDto(Proficiencia entity);
    @Mapping(target = "id", ignore = true)
    void updateEntity(@MappingTarget Proficiencia target, ProficienciaDTO source);
}
