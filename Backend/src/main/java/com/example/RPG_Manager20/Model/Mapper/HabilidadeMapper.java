package com.example.RPG_Manager20.Model.Mapper;

import com.example.RPG_Manager20.Model.DTO.HabilidadeDTO;
import com.example.RPG_Manager20.Model.DTO.Response.HabilidadeResponseDTO;
import com.example.RPG_Manager20.Model.Entities.Habilidade;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")

public interface HabilidadeMapper {

    // Converter DTO para Entity (criação)
    @Mapping(target = "id", ignore = true)
    Habilidade toEntity(HabilidadeDTO habilidadeDTO);

    // Converter Entity para DTO
    HabilidadeDTO toDto(Habilidade entity);

    HabilidadeResponseDTO toResponseDto(Habilidade entity);
    @Mapping(target = "id", ignore = true)
    void updateEntity(@MappingTarget Habilidade target, HabilidadeDTO source);
}
