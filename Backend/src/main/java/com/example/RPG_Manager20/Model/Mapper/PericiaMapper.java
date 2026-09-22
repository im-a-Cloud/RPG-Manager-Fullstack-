package com.example.RPG_Manager20.Model.Mapper;

import com.example.RPG_Manager20.Model.DTO.PericiaDTO;
import com.example.RPG_Manager20.Model.DTO.Request.PericiaPersonagemRequestDTO;
import com.example.RPG_Manager20.Model.Entities.Pericia;
import com.example.RPG_Manager20.Model.Entities.PersonagemPericia;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface PericiaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "personagem", ignore = true)
    @Mapping(target = "pericia", ignore = true)
    @Mapping(target = "proficiente", source = "isProficiente")
    PersonagemPericia toEntity(PericiaPersonagemRequestDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Pericia toEntity(PericiaDTO dto);

    PericiaDTO toDto(Pericia entity);

    @Named("mapPericias")
    default List<PersonagemPericia> mapPericias(List<PericiaPersonagemRequestDTO> dtos) {
        if (dtos == null) {
            return new ArrayList<>();
        }
        List<PersonagemPericia> result = new ArrayList<>();
        for (PericiaPersonagemRequestDTO dto : dtos) {
            result.add(toEntity(dto));
        }
        return result;
    }
}