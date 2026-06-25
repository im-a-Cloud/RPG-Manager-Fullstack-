package com.example.RPG_Manager20.Controller;

import com.example.RPG_Manager20.Model.DTO.ClasseDTO;
import com.example.RPG_Manager20.Model.Entities.Classe;
import com.example.RPG_Manager20.Model.Mapper.ClasseMapper;
import com.example.RPG_Manager20.Service.ClasseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/classe")
public class ClasseController {

    @Autowired
    private ClasseService classeService;

    @Autowired
    private ClasseMapper classeMapper;

    // POST - Criar classe com proficiências
    @PostMapping
    public ResponseEntity<ClasseDTO> criarClasse(@RequestBody ClasseDTO classeDTO) {
        System.out.println("Recebido: " + classeDTO);
        ClasseDTO savedClasse = classeService.criarClasse(classeDTO);
        return new ResponseEntity<>(savedClasse, HttpStatus.CREATED);
    }

    // POST - Criar classe (endpoint alternativo)
    @PostMapping("/criar")
    public ResponseEntity<ClasseDTO> criarClasseAlternativo(@RequestBody ClasseDTO classeDTO) {
        System.out.println("Recebido: " + classeDTO);
        ClasseDTO savedClasse = classeService.criarClasse(classeDTO);
        return new ResponseEntity<>(savedClasse, HttpStatus.CREATED);
    }

    // GET - Listar todas as classes
    @GetMapping
    public ResponseEntity<List<ClasseDTO>> listarTodas() {
        List<ClasseDTO> classes = classeService.list()
                .stream()
                .map(classeMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(classes);
    }

    // GET - Listar todas (endpoint alternativo)
    @GetMapping("/listarTodos")
    public ResponseEntity<List<ClasseDTO>> listarTodos() {
        List<ClasseDTO> classes = classeService.list()
                .stream()
                .map(classeMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(classes);
    }

    // GET - Buscar classe por ID
    @GetMapping("/{idClasse}")
    public ResponseEntity<ClasseDTO> buscarClasse(@PathVariable("idClasse") Long idClasse) {
        Classe classe = classeService.findById(idClasse);
        return ResponseEntity.ok(classeMapper.toDto(classe));
    }

    // GET - Buscar classe por ID (endpoint alternativo)
    @GetMapping("/listar/{idClasse}")
    public ResponseEntity<ClasseDTO> getClasse(@PathVariable("idClasse") Long idClasse) {
        Classe classe = classeService.findById(idClasse);
        return ResponseEntity.ok(classeMapper.toDto(classe));
    }

    // PUT - Atualizar classe
    @PutMapping("/{idClasse}")
    public ResponseEntity<ClasseDTO> atualizarClasse(
            @PathVariable("idClasse") Long idClasse,
            @RequestBody ClasseDTO classeDTO) {
        Classe updatedClasse = classeService.update(classeDTO, idClasse);
        return ResponseEntity.ok(classeMapper.toDto(updatedClasse));
    }

    // DELETE - Deletar classe
    @DeleteMapping("/{idClasse}")
    public ResponseEntity<Void> deletarClasse(@PathVariable("idClasse") Long idClasse) {
        classeService.delete(idClasse);
        return ResponseEntity.noContent().build();
    }
}