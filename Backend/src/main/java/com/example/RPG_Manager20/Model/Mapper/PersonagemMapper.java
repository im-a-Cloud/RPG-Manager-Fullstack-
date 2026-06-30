package com.example.RPG_Manager20.Model.Mapper;

import com.example.RPG_Manager20.Model.DTO.PersonagemDTO;
import com.example.RPG_Manager20.Model.DTO.Request.PersonagemRequestDTO;
import com.example.RPG_Manager20.Model.DTO.Response.PersonagemResponseDTO;
import com.example.RPG_Manager20.Model.DTO.Summary.PersonagemSummaryDTO;
import com.example.RPG_Manager20.Model.Entities.Personagem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PersonagemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "proficienciasPersonagem", ignore = true)
    @Mapping(target = "classePersonagem", ignore = true)
    Personagem toEntity(PersonagemDTO dto);

    @Mapping(target = "bonusProficiencia", ignore = true)
    @Mapping(target = "bonusForca", ignore = true)
    @Mapping(target = "bonusDestreza", ignore = true)
    @Mapping(target = "bonusConstituicao", ignore = true)
    @Mapping(target = "bonusInteligencia", ignore = true)
    @Mapping(target = "bonusSabedoria", ignore = true)
    @Mapping(target = "bonusCarisma", ignore = true)
    PersonagemDTO toDto(Personagem entity);

    PersonagemResponseDTO toResponseDto(Personagem entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "classePersonagem", ignore = true)
    Personagem toEntity(PersonagemRequestDTO requestDTO);

    @Mapping(target = "nomeClasse", source = "classePersonagem.nomeClasse")
    PersonagemSummaryDTO toSummaryDTO(Personagem entity);
}