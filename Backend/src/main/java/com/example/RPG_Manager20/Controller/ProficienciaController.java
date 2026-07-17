package com.example.RPG_Manager20.Controller;

import com.example.RPG_Manager20.Model.DTO.ProficienciaDTO;
import com.example.RPG_Manager20.Model.Entities.Proficiencia;
import com.example.RPG_Manager20.Model.Mapper.ProficienciaMapper;
import com.example.RPG_Manager20.Service.ProficienciaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/proficiencias")
public class ProficienciaController {

    @Autowired
    private ProficienciaMapper proficienciaMapper;

    @Autowired
    private ProficienciaService proficienciaService;

    // Construtor corrigido
    @Autowired
    public ProficienciaController(ProficienciaService proficienciaService) {
    }

    // POST - Criar proficiência
    @PostMapping
    public ResponseEntity<ProficienciaDTO> criarProficiencia(@Valid @RequestBody ProficienciaDTO proficienciaDTO) {
        System.out.println("Recebido: " + proficienciaDTO);

        Proficiencia proficiencia = proficienciaMapper.toEntity(proficienciaDTO);
        Proficiencia savedProficiencia = proficienciaService.save(proficiencia);

        return new ResponseEntity<>(proficienciaMapper.toDto(savedProficiencia), HttpStatus.CREATED);
    }

    // GET - Listar todas
    @GetMapping
    public ResponseEntity<List<ProficienciaDTO>> listarTodas() {
        List<ProficienciaDTO> proficiencias = proficienciaService.list()
                .stream()
                .map(proficienciaMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(proficiencias);
    }

    @PutMapping("/{idProficiencia}")
    public ResponseEntity<ProficienciaDTO> atualizar(@Valid @RequestBody ProficienciaDTO proficienciaDTO, Long idProficiencia) {
        Proficiencia proficiencia = proficienciaMapper.toEntity(proficienciaDTO);
        Proficiencia updatedProficiencia = proficienciaService.update(proficienciaDTO, idProficiencia);
        ProficienciaDTO proficienciaDTOUpdated = proficienciaMapper.toDto(updatedProficiencia);
        return new ResponseEntity<>(proficienciaDTOUpdated, HttpStatus.OK);
    }

    // GET - Listar todas (endpoint alternativo)
    @GetMapping("/listar")
    public ResponseEntity<List<ProficienciaDTO>> listarProficiencias() {
        List<ProficienciaDTO> proficiencias = proficienciaService.list()
                .stream()
                .map(proficienciaMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(proficiencias);
    }

    // GET - Buscar por ID
    @GetMapping("/{idProficiencia}")
    public ResponseEntity<ProficienciaDTO> buscarProficiencia(@PathVariable("idProficiencia") Long idProficiencia) {
        Proficiencia proficiencia = proficienciaService.findById(idProficiencia);
        return ResponseEntity.ok(proficienciaMapper.toDto(proficiencia));
    }

    // GET - Buscar por ID (endpoint alternativo)
    @GetMapping("/listar/{idProficiencia}")
    public ResponseEntity<ProficienciaDTO> getProficiencia(@PathVariable("idProficiencia") Long idProficiencia) {
        Proficiencia proficiencia = proficienciaService.findById(idProficiencia);
        return ResponseEntity.ok(proficienciaMapper.toDto(proficiencia));
    }

}