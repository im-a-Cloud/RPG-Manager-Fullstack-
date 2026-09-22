package com.example.RPG_Manager20.Controller;

import com.example.RPG_Manager20.Model.DTO.PericiaDTO;
import com.example.RPG_Manager20.Model.DTO.Response.PericiaResponseDTO;
import com.example.RPG_Manager20.Model.Entities.Pericia;
import com.example.RPG_Manager20.Model.Mapper.PericiaMapper;  // ← NOVO MAPPER
import com.example.RPG_Manager20.Repository.PericiaRepository;
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

    private final PericiaRepository periciaRepository;

    public PericiaController(PericiaRepository periciaRepository) {
        this.periciaRepository = periciaRepository;
    }

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
    public List<PericiaResponseDTO> listar() {
        return periciaRepository.findAllByOrderByNomeExibicaoAsc()
                .stream()
                .map(PericiaResponseDTO::from)
                .toList();
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