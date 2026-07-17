package com.example.RPG_Manager20.Model.Mapper;

import com.example.RPG_Manager20.Model.DTO.ClasseDTO;
import com.example.RPG_Manager20.Model.Entities.Classe;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;  // ← ADICIONE ESTE IMPORT!


@Mapper(componentModel = "spring")
public interface ClasseMapper {

    ClasseMapper INSTANCE = Mappers.getMapper(ClasseMapper.class);

    // 🔥 MAPEIA O ID
    @Mapping(source = "id", target = "id")
    ClasseDTO toDto(Classe classe);

    @Mapping(target = "id", ignore = true)
    Classe toEntity(ClasseDTO classeDTO);
}