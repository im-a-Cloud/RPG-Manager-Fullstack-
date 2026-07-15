package com.example.RPG_Manager20.Controller;

import com.example.RPG_Manager20.Model.DTO.AdicionarProficienciaRequestDTO;
import com.example.RPG_Manager20.Model.DTO.MagiaDTO;
import com.example.RPG_Manager20.Model.DTO.PersonagemDTO;
import com.example.RPG_Manager20.Model.DTO.ProficienciaDTO;
import com.example.RPG_Manager20.Model.DTO.Request.PersonagemRequestDTO;
import com.example.RPG_Manager20.Model.DTO.Response.PersonagemResponseDTO;
import com.example.RPG_Manager20.Model.DTO.Summary.PersonagemSummaryDTO;
import com.example.RPG_Manager20.Model.Entities.Magia;
import com.example.RPG_Manager20.Model.Entities.Personagem;
import com.example.RPG_Manager20.Model.Entities.Proficiencia;
import com.example.RPG_Manager20.Model.Mapper.MagiaMapper;
import com.example.RPG_Manager20.Model.Mapper.PersonagemMapper;
import com.example.RPG_Manager20.Model.Mapper.ProficienciaMapper;
import com.example.RPG_Manager20.Service.PersonagemMagiaService;
import com.example.RPG_Manager20.Service.PersonagemProficienciaService;
import com.example.RPG_Manager20.Service.PersonagemService;
import com.example.RPG_Manager20.Service.ProficienciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/personagem")
@RequiredArgsConstructor
@Tag(name = "Personagens", description = "API de gerenciamento de personagens")
@CrossOrigin(origins = "http://localhost:4200")  // ← PERMITE ANGULAR
public class PersonagemController {

    @Autowired
    private PersonagemService personagemService;

    @Autowired
    private PersonagemMagiaService personagemMagiaService;

    @Autowired
    private PersonagemProficienciaService personagemProficienciaService;

    @Autowired
    private PersonagemMapper personagemMapper;

    @Autowired
    private MagiaMapper magiaMapper;

    @Autowired
    private ProficienciaMapper proficienciaMapper;

    @PostMapping("/criar")
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

    @GetMapping("/listar")
    @Operation(summary = "Listar todos os personagens")
    public ResponseEntity<List<PersonagemSummaryDTO>> listarTodos() {
        List<PersonagemSummaryDTO> lista = personagemService.listarPersonagens();
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
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        personagemService.deletarPersonagem(id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/{id}/magias/{magiaId}")
    public ResponseEntity<PersonagemDTO> adicionarMagia(
            @PathVariable Long id,
            @PathVariable Long magiaId) {

        Personagem personagem = personagemMagiaService.adicionarMagia(id, magiaId);
        return ResponseEntity.ok(personagemMapper.toDto(personagem));
    }

    @GetMapping("/{id}/magias")
    public ResponseEntity<List<MagiaDTO>> listarMagias(@PathVariable Long id) {
        List<Magia> magias = personagemMagiaService.listarMagiasDoPersonagem(id);
        return ResponseEntity.ok(magias.stream().map(magiaMapper::toDto).toList());
    }

    @PostMapping("{idPersonagem}/proficiencias")
    public ResponseEntity<PersonagemDTO> adicionarProficiencias
            (@PathVariable Long idPersonagem, @RequestBody AdicionarProficienciaRequestDTO request){

        Personagem personagem = personagemProficienciaService.adicionarProficiencia(idPersonagem, request);
        return ResponseEntity.ok(personagemMapper.toDto(personagem));
    }
    @GetMapping("{idPersonagem}/proficiencias")
    public ResponseEntity<List<ProficienciaDTO>> listarProficiencias(@PathVariable Long idPersonagem){
        List<Proficiencia> proficiencias = personagemProficienciaService.listaProficiencias(idPersonagem);
        return ResponseEntity.ok(proficiencias.stream().map(proficienciaMapper::toDto).toList());
    }
}