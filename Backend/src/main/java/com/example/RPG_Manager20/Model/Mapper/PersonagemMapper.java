package com.example.RPG_Manager20.Model.Mapper;

import com.example.RPG_Manager20.Model.DTO.Request.PersonagemRequestDTO;
import com.example.RPG_Manager20.Model.DTO.Response.PersonagemResponseDTO;
import com.example.RPG_Manager20.Model.Entities.Personagem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface PersonagemMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "classePersonagem", ignore = true)
    @Mapping(target = "caPersonagem", source = "ca")
    @Mapping(target = "iniciativaPersonagem", source = "iniciativa")
    @Mapping(target = "movimentoPersonagem", source = "movimento")
    @Mapping(target = "pontosVidaPersonagem", source = "pontosVida")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "magias", ignore = true)
    @Mapping(target = "inventarioPersonagem", ignore = true)
    @Mapping(target = "periciasPersonagem", ignore = true)
    @Mapping(target = "habilidadesPersonagem", ignore = true)
    @Mapping(target = "proficienciasPersonagem", ignore = true)
    Personagem toEntity(PersonagemRequestDTO dto);

    default PersonagemResponseDTO toResponseDto(Personagem personagem) {
        if (personagem == null) {
            return null;
        }
        return PersonagemResponseDTO.from(personagem, personagem.getClassePersonagem());
    }
}