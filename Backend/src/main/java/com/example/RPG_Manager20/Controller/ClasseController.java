package com.example.RPG_Manager20.Controller;

import com.example.RPG_Manager20.Model.DTO.ClasseDTO;
import com.example.RPG_Manager20.Model.DTO.Response.ClasseResponseDTO;
import com.example.RPG_Manager20.Model.Entities.Classe;
import com.example.RPG_Manager20.Model.Mapper.ClasseMapper;
import com.example.RPG_Manager20.Repository.ClasseRepository;
import com.example.RPG_Manager20.Service.ClasseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/classes")  // ← MUDAR PARA "/classes" (plural e consistente)
@CrossOrigin(origins = "http://localhost:4200")
public class ClasseController {

    @Autowired
    private ClasseService classeService;

    @Autowired
    private ClasseMapper classeMapper;

    @Autowired
    private ClasseRepository classeRepository;

    // ============================================
    // POST - Criar classe(MANTER PARA CASOS FUTUTOS)
    // ============================================
    @PostMapping
    public ResponseEntity<ClasseDTO> criarClasse(@RequestBody ClasseDTO classeDTO) {
        System.out.println("📤 Recebendo classe: " + classeDTO);
        ClasseDTO savedClasse = classeService.criarClasse(classeDTO);
        return new ResponseEntity<>(savedClasse, HttpStatus.CREATED);
    }

    // ============================================
    // GET - Listar todas as classes
    // ============================================
    @GetMapping
    public ResponseEntity<List<ClasseDTO>> listarTodas() {
        System.out.println("📋 GET /classes - Listando todas as classes");
        List<ClasseDTO> classes = classeService.listarTodas();
        return ResponseEntity.ok(classes);
    }
    @GetMapping("/{idClasse}")
    public ResponseEntity<ClasseDTO> buscarClasse(@PathVariable("idClasse") Long idClasse) {
        try {
            ClasseDTO classe = classeService.buscarPorId(idClasse);
            return ResponseEntity.ok(classe);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @DeleteMapping("/{idClasse}")
    public ResponseEntity<Void> deletarClasse(@PathVariable("idClasse") Long idClasse) {
        classeService.deletarClasse(idClasse);
        return ResponseEntity.noContent().build();
    }
}