package com.example.RPG_Manager20.Model.Mapper;

import com.example.RPG_Manager20.Model.DTO.ItemDTO;
import com.example.RPG_Manager20.Model.DTO.Request.ItemRequestDTO;
import com.example.RPG_Manager20.Model.DTO.Response.ItemResponseDTO;
import com.example.RPG_Manager20.Model.Entities.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    @Mapping(target = "id", ignore = true)
    Item toEntity(ItemRequestDTO requestDTO);

    ItemResponseDTO toDto(Item entity);

    ItemResponseDTO toResponseDto(Item entity);

    @Mapping(target = "id", ignore = true)
    Item toEntity(ItemDTO dto);
}
