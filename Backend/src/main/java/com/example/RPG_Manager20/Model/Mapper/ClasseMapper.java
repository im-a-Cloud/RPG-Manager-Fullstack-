package com.example.RPG_Manager20.Model.Mapper;

import com.example.RPG_Manager20.Model.DTO.ClasseDTO;
import com.example.RPG_Manager20.Model.Entities.Classe;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClasseMapper {

    @Mapping(target = "conjurador", source = "isConjurador")
    Classe toEntity(ClasseDTO dto);

    @Mapping(target = "isConjurador", source = "conjurador")
    ClasseDTO toDto(Classe entity);
}