package com.example.RPG_Manager20.Controller;

import com.example.RPG_Manager20.Model.DTO.AdicionarPericiaRequestDTO;
import com.example.RPG_Manager20.Model.DTO.Request.PericiaRequestDTO;
import com.example.RPG_Manager20.Model.DTO.Response.PericiaResponseDTO;
import com.example.RPG_Manager20.Model.DTO.Response.PersonagemResponseDTO;
import com.example.RPG_Manager20.Model.Entities.Personagem;
import com.example.RPG_Manager20.Service.PersonagemPericiaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/personagem/{personagemId}/pericias")
public class PericiaPersonagemController {
    @Autowired
    private PersonagemPericiaService personagemPericiaService;

    @GetMapping
    public ResponseEntity<List<PericiaResponseDTO>> listarPericias(@PathVariable Long personagemId) {
        List<PericiaResponseDTO> pericias = personagemPericiaService.listarPericiaPersonagem(personagemId);
        return ResponseEntity.ok(pericias);
    }

    @PostMapping("/adicionar")
    public ResponseEntity<PersonagemResponseDTO> adicionarPericia(@PathVariable Long personagemId, @Valid @RequestBody AdicionarPericiaRequestDTO adicionarPericiaRequestDTO) {
        PersonagemResponseDTO response = personagemPericiaService.addPersonagemPericia(personagemId, adicionarPericiaRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PutMapping("/{periciaId}/proficiencia")
    public ResponseEntity<PersonagemResponseDTO> atualizarProficiencia(
            @PathVariable Long personagemId,
            @PathVariable Long periciaId,
            @RequestParam boolean isProficiente) {

        PersonagemResponseDTO response = personagemPericiaService.atualizarProficiencia(personagemId, periciaId, isProficiente);
        return ResponseEntity.ok(response);
    }
}
