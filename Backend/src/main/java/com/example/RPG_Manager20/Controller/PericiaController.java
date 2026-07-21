package com.example.RPG_Manager20.Controller;

import com.example.RPG_Manager20.Model.DTO.PericiaDTO;
import com.example.RPG_Manager20.Model.Entities.Pericia;
import com.example.RPG_Manager20.Model.Mapper.PericiaMapper;  // ← NOVO MAPPER
import com.example.RPG_Manager20.Service.PericiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pericias")  // ← PLURAL (padrão REST)
@CrossOrigin(origins = "http://localhost:4200")
public class PericiaController {

    @Autowired
    private PericiaService periciaService;

    @Autowired
    private PericiaMapper periciaEntityMapper;  // ← MAPPER CORRETO

    // ============================================
    // POST - CRIAR PERÍCIA
    // ============================================
    @PostMapping
    public ResponseEntity<PericiaDTO> criar(@RequestBody PericiaDTO periciaDTO) {
        Pericia pericia = periciaEntityMapper.toEntity(periciaDTO);
        pericia = periciaService.save(pericia);
        return ResponseEntity.status(HttpStatus.CREATED).body(periciaEntityMapper.toDto(pericia));
    }

    // ============================================
    // GET - BUSCAR POR ID
    // ============================================
    @GetMapping("/{idPericia}")
    public ResponseEntity<PericiaDTO> buscarPorId(@PathVariable("idPericia") Long idPericia) {
        Pericia pericia = periciaService.findById(idPericia);
        return ResponseEntity.ok(periciaEntityMapper.toDto(pericia));
    }

    // ============================================
    // GET - LISTAR TODOS
    // ============================================
    @GetMapping
    public ResponseEntity<List<PericiaDTO>> listarTodos() {
        List<PericiaDTO> pericias = periciaService.list().stream()
                .map(periciaEntityMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(pericias);
    }

    // ============================================
    // PUT - ATUALIZAR
    // ============================================
    @PutMapping("/{idPericia}")
    public ResponseEntity<PericiaDTO> atualizar(
            @PathVariable("idPericia") Long idPericia,
            @RequestBody PericiaDTO periciaDTO) {
        Pericia pericia = periciaEntityMapper.toEntity(periciaDTO);
        pericia.setId(idPericia);
        pericia = periciaService.save(pericia);
        return ResponseEntity.ok(periciaEntityMapper.toDto(pericia));
    }

    // ============================================
    // DELETE - DELETAR
    // ============================================
    @DeleteMapping("/{idPericia}")
    public ResponseEntity<Void> deletar(@PathVariable("idPericia") Long idPericia) {
        periciaService.delete(idPericia);
        return ResponseEntity.noContent().build();
    }
}