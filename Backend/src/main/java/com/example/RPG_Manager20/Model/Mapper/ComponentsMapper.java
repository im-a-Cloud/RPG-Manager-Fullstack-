package com.example.RPG_Manager20.Model.Mapper;

import com.example.RPG_Manager20.Model.DTO.ComponentsDTO;
import com.example.RPG_Manager20.Model.Entities.Components;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ComponentsMapper {

    ComponentsDTO toDto(Components components);

    Components toEntity(ComponentsDTO dto);
}