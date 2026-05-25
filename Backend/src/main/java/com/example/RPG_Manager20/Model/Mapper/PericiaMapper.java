package com.example.RPG_Manager20.Model.Mapper;

import com.example.RPG_Manager20.Model.DTO.PericiaDTO;
import com.example.RPG_Manager20.Model.Entities.Pericia;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface PericiaMapper {
    Pericia toEntity(PericiaDTO periciaDTO);
    PericiaDTO toDto(Pericia entity);
}
