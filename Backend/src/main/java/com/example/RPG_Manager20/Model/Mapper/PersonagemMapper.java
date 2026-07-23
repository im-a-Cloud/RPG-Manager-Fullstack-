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
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {HabilidadeMapper.class, ProficienciaMapper.class, ItemMapper.class, MagiaMapper.class, PericiaMapper.class}
)
public interface PersonagemMapper {

    // ============================================
    // REQUEST DTO → ENTITY
    // ============================================
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "classePersonagem", ignore = true)

    // Mapeia os campos de combate
    @Mapping(target = "caPersonagem", source = "ca")
    @Mapping(target = "iniciativaPersonagem", source = "iniciativa")
    @Mapping(target = "movimentoPersonagem", source = "movimento")
    @Mapping(target = "pontosVidaPersonagem", source = "pontosVida")

    // 🔥 LISTAS - MAPEAMENTO
    @Mapping(source = "habilidades", target = "habilidades")
    @Mapping(source = "proficiencias", target = "proficienciasPersonagem")
    @Mapping(source = "inventario", target = "inventarioPersonagem")
    @Mapping(source = "magias", target = "magias")
    @Mapping(source = "pericias", target = "periciasPersonagem", qualifiedByName = "mapPericias")
    Personagem toEntity(PersonagemRequestDTO dto);

    // ============================================
    // ENTITY → RESPONSE DTO
    // ============================================
    default PersonagemResponseDTO toResponseDto(Personagem personagem) {
        if (personagem == null) {
            return null;
        }
        return PersonagemResponseDTO.from(personagem, personagem.getClassePersonagem());
    }
}