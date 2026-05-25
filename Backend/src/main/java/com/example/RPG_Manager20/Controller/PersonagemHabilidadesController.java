package com.example.RPG_Manager20.Controller;

import com.example.RPG_Manager20.Model.DTO.HabilidadeDTO;
import com.example.RPG_Manager20.Model.DTO.Response.HabilidadeResponseDTO;
import com.example.RPG_Manager20.Model.DTO.Response.PersonagemResponseDTO;
import com.example.RPG_Manager20.Service.PersonagemHabilidadeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/personagem/{personagemId}/habilidades")
public class PersonagemHabilidadesController {

    @Autowired
    private PersonagemHabilidadeService personagemHabilidadeService;

    @GetMapping
    public ResponseEntity<List<HabilidadeResponseDTO>> listarHabilidades(@PathVariable Long personagemId) {
        List<HabilidadeResponseDTO> habilidades = personagemHabilidadeService.listarHabilidadesDoPersonagem(personagemId);
        return ResponseEntity.ok(habilidades);
    }

    @PostMapping("/adicionar/{habilidadeId}")
    public ResponseEntity<PersonagemResponseDTO> adicionarHabilidade(
            @PathVariable Long personagemId,
            @PathVariable Long habilidadeId) {

        PersonagemResponseDTO response = personagemHabilidadeService.adicionarHabilidade(personagemId, habilidadeId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/remover/{habilidadeId}")
    public ResponseEntity<PersonagemResponseDTO> deletarHabilidade(
            @PathVariable Long personagemId,
            @PathVariable Long habilidadeId) {

        PersonagemResponseDTO response = personagemHabilidadeService.removerHabilidade(personagemId, habilidadeId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/atualizar/{habilidadeId}")
    public ResponseEntity<HabilidadeResponseDTO> atualizarHabilidade(
            @PathVariable Long personagemId,
            @PathVariable Long habilidadeId,
            @Valid @RequestBody HabilidadeDTO dto) {

        HabilidadeResponseDTO response = personagemHabilidadeService.atualizarHabilidade(habilidadeId, dto);
        return ResponseEntity.ok(response);
    }
}