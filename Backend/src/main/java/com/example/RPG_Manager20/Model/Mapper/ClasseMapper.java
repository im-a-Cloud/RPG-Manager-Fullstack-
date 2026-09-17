package com.example.RPG_Manager20.Model.Mapper;

import com.example.RPG_Manager20.Model.DTO.ClasseDTO;
import com.example.RPG_Manager20.Model.Entities.Classe;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;  // ← ADICIONE ESTE IMPORT!


@Mapper(componentModel = "spring")
public interface ClasseMapper {

    ClasseMapper INSTANCE = Mappers.getMapper(ClasseMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "isConjurador", target = "conjurador")  // ← ADICIONAR!
    @Mapping(target = "listaProficienciasClasse", ignore = true)
    Classe toEntity(ClasseDTO dto);

    @Mapping(source = "conjurador", target = "isConjurador")  // ← ADICIONAR!
    ClasseDTO toDto(Classe entity);
}