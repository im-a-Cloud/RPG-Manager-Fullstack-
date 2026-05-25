package com.example.RPG_Manager20.Model.Mapper;

import com.example.RPG_Manager20.Model.DTO.ComponentsDTO;
import com.example.RPG_Manager20.Model.DTO.MagiaDTO;
import com.example.RPG_Manager20.Model.DTO.MagiaListDTO;
import com.example.RPG_Manager20.Model.Entities.Components;
import com.example.RPG_Manager20.Model.Entities.Magia;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MagiaMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "casting_time", source = "casting_time")
    @Mapping(target = "components", source = "components")
    Magia toEntity(MagiaDTO dto);

    @Mapping(target = "casting_time", source = "casting_time")
    @Mapping(target = "components", source = "components")
    MagiaDTO toDto(Magia magia);


    MagiaListDTO toListDTO(Magia magia);

    //Mapear componentes
    Components toComponents(ComponentsDTO dto);
    ComponentsDTO toComponentsDto(Components entity);

    List<MagiaListDTO> toListDTOList(List<Magia> magias);
    void updateEntity(@MappingTarget Magia target, MagiaDTO source);
}
