package com.example.RPG_Manager20.Controller;

import com.example.RPG_Manager20.Model.DTO.Response.ItemResponseDTO;
import com.example.RPG_Manager20.Model.DTO.Response.PersonagemResponseDTO;
import com.example.RPG_Manager20.Service.InventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/personagem/{idPersonagem}/inventario")
@CrossOrigin(origins = "http://localhost:4200")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    // ============================================
    // LISTAR INVENTÁRIO
    // ============================================
    @GetMapping
    public ResponseEntity<List<ItemResponseDTO>> listarInventario(
            @PathVariable("idPersonagem") Long idPersonagem) {

        return ResponseEntity.ok(inventarioService.listarInventario(idPersonagem));
    }

    // ============================================
    // ADICIONAR ITEM
    // ============================================
    @PostMapping("/itens/{itemId}")
    public ResponseEntity<PersonagemResponseDTO> adicionarInventario(
            @PathVariable("idPersonagem") Long idPersonagem,
            @PathVariable Long itemId,
            @RequestParam(defaultValue = "1") int quantidade) {

        PersonagemResponseDTO response =
                inventarioService.adicionarItemIventario(idPersonagem, itemId, quantidade);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ============================================
    // REMOVER ITEM
    // ============================================
    @DeleteMapping("/itens/{idItem}")
    public ResponseEntity<PersonagemResponseDTO> removerItem(
            @PathVariable("idPersonagem") Long idPersonagem,
            @PathVariable Long idItem) {

        return ResponseEntity.ok(inventarioService.removerItem(idPersonagem, idItem));
    }

    // ============================================
    // BUSCAR ITEM POR NOME
    // ============================================
    @GetMapping("/itens/{nomeItem}")
    public ResponseEntity<ItemResponseDTO> buscarItemNoInventario(
            @PathVariable("idPersonagem") Long idPersonagem,
            @PathVariable String nomeItem) {

        return ResponseEntity.ok(inventarioService.buscarItemIventario(idPersonagem, nomeItem));
    }

    // ============================================
    // ATUALIZAR QUANTIDADE
    // ============================================
    @PutMapping("/itens/{itemId}/quantidade")
    public ResponseEntity<PersonagemResponseDTO> atualizarQuantidade(
            @PathVariable("idPersonagem") Long idPersonagem,
            @PathVariable Long itemId,
            @RequestParam int quantidade) {

        return ResponseEntity.ok(
                inventarioService.atualizarQuantidadeItem(idPersonagem, itemId, quantidade));
    }
}