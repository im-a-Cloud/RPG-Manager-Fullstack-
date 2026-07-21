package com.example.RPG_Manager20.Model.Mapper;

import com.example.RPG_Manager20.Model.DTO.PericiaDTO;
import com.example.RPG_Manager20.Model.DTO.PericiaPersonagemDTO;
import com.example.RPG_Manager20.Model.Entities.Pericia;
import com.example.RPG_Manager20.Model.Entities.PersonagemPericia;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface PericiaMapper {

    // ============================================
    // DTO → ENTITY (IGNORA A PERÍCIA)
    // ============================================
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "personagem", ignore = true)
    @Mapping(target = "pericia", ignore = true)  // ← IGNORA!
    PersonagemPericia toEntity(PericiaPersonagemDTO dto);

    // ============================================
    // ENTITY → DTO
    // ============================================
    @Mapping(source = "pericia.id", target = "pericia.id")
    @Mapping(source = "pericia.nomePericia", target = "pericia.nomePericia")
    @Mapping(source = "pericia.atributoChave", target = "pericia.atributoChave")
    PericiaPersonagemDTO toDto(PersonagemPericia entity);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Pericia toEntity(PericiaDTO dto);
    PericiaDTO toDto(Pericia entity);

    // ============================================
    // MÉTODO PARA MAPEAR LISTA DE PERÍCIAS
    // ============================================
    @Named("mapPericias")
    default List<PersonagemPericia> mapPericias(List<PericiaPersonagemDTO> dtos) {
        if (dtos == null) {
            return new ArrayList<>();
        }
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}