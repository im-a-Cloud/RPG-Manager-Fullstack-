package com.example.RPG_Manager20.Controller;

import com.example.RPG_Manager20.Model.DTO.Request.ItemRequestDTO;
import com.example.RPG_Manager20.Model.DTO.Response.ItemResponseDTO;
import com.example.RPG_Manager20.Model.Mapper.ItemMapper;
import com.example.RPG_Manager20.Service.ItemService;
import com.example.RPG_Manager20.Service.InventarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/itens")
@CrossOrigin(origins = "http://localhost:4200")  // ← PERMITE ANGULAR
public class ItemController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private InventarioService inventarioService;

    @PostMapping
    public ResponseEntity<ItemResponseDTO> crate(@Valid @RequestBody ItemRequestDTO itemRequestDTO) {
        ItemResponseDTO responseDTO = inventarioService.criarItem(itemRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/listarTodos")
    public ResponseEntity<List<ItemResponseDTO>> listarTodos() {
        List<ItemResponseDTO> itens = itemService.list().stream()
                .map(itemMapper::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(itens);
    }

    @GetMapping("/listar/{idItem}")
    public ResponseEntity<ItemResponseDTO> getItem(@PathVariable("idItem") Long idItem) {
        return ResponseEntity.ok(itemMapper.toResponseDto(itemService.findById(idItem)));
    }
}