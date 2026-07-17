package com.example.RPG_Manager20.Controller;

import com.example.RPG_Manager20.Model.DTO.Request.PersonagemRequestDTO;
import com.example.RPG_Manager20.Model.DTO.Response.PersonagemResponseDTO;
import com.example.RPG_Manager20.Service.PersonagemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/personagens")
@RequiredArgsConstructor
@Tag(name = "Personagens", description = "API de gerenciamento de personagens")
@CrossOrigin(origins = "http://localhost:4200")
public class PersonagemController {

    private final PersonagemService personagemService;

    @PostMapping
    @Operation(summary = "Criar novo personagem")
    public ResponseEntity<PersonagemResponseDTO> criar(@Valid @RequestBody PersonagemRequestDTO requestDTO) {
        PersonagemResponseDTO response = personagemService.criarPersonagem(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar personagem por ID")
    public ResponseEntity<PersonagemResponseDTO> buscarPorId(@PathVariable Long id) {
        PersonagemResponseDTO response = personagemService.buscarPersonagemPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Listar todos os personagens")
    public ResponseEntity<List<PersonagemResponseDTO>> listarTodos() {
        List<PersonagemResponseDTO> lista = personagemService.listarTodos();
        return ResponseEntity.ok(lista);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar personagem")
    public ResponseEntity<PersonagemResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PersonagemRequestDTO requestDTO) {
        PersonagemResponseDTO response = personagemService.atualizarPersonagem(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar personagem")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        personagemService.deletarPersonagem(id);
        return ResponseEntity.noContent().build();
    }
}