package com.example.RPG_Manager20.Controller;

import com.example.RPG_Manager20.Model.DTO.Response.ItemResponseDTO;
import com.example.RPG_Manager20.Model.DTO.Response.PersonagemResponseDTO;
import com.example.RPG_Manager20.Model.Entities.Personagem;
import com.example.RPG_Manager20.Model.Mapper.PersonagemMapper;
import com.example.RPG_Manager20.Service.InventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/personagem/{idPersonagem}/inventario")  // ← usa {idPersonagem}
@CrossOrigin(origins = "http://localhost:4200")  // ← PERMITE ANGULAR
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @Autowired
    private PersonagemMapper personagemMapper;

    // Listar inventário
    @GetMapping
    public ResponseEntity<List<ItemResponseDTO>> listarInventario(
            @PathVariable("idPersonagem") Long idPersonagem) {  // ← nome consistente
        List<ItemResponseDTO> inventario = inventarioService.listarInventario(idPersonagem);
        return ResponseEntity.ok(inventario);
    }

    // Adicionar item ao inventário
    @PostMapping("/itens/{itemId}")
    public ResponseEntity<PersonagemResponseDTO> adicionarInventario(
            @PathVariable("idPersonagem") Long idPersonagem,  // ← mesmo nome do RequestMapping
            @PathVariable Long itemId,
            @RequestParam(defaultValue = "1") int quantidade) {

        Personagem personagem = inventarioService.adicionarItemIventario(idPersonagem, itemId, quantidade);
        return ResponseEntity.status(HttpStatus.CREATED).body(personagemMapper.toResponseDto(personagem));
    }

    // Remover item do inventário
    @DeleteMapping("/itens/{idItem}")
    public ResponseEntity<PersonagemResponseDTO> removerItem(
            @PathVariable("idPersonagem") Long idPersonagem,  // ← mesmo nome
            @PathVariable Long idItem) {

        Personagem personagem = inventarioService.removerItem(idPersonagem, idItem);
        return ResponseEntity.ok(personagemMapper.toResponseDto(personagem));
    }

    // Buscar item específico no inventário
    @GetMapping("/itens/{idItem}")
    public ResponseEntity<ItemResponseDTO> buscarItemNoInventario(
            @PathVariable("idPersonagem") Long idPersonagem,  // ← mesmo nome
            @PathVariable Long idItem) {

        ItemResponseDTO item = inventarioService.buscarItemIventario(idPersonagem, idItem);
        return ResponseEntity.ok(item);
    }

    // Atualizar quantidade de um item
    @PutMapping("/itens/{itemId}/quantidade")
    public ResponseEntity<PersonagemResponseDTO> atualizarQuantidade(
            @PathVariable("idPersonagem") Long idPersonagem,  // ← mesmo nome
            @PathVariable Long itemId,
            @RequestParam int quantidade) {

        Personagem personagem = inventarioService.atualizarQuantidadeItem(idPersonagem, itemId, quantidade);
        return ResponseEntity.ok(personagemMapper.toResponseDto(personagem));
    }
}