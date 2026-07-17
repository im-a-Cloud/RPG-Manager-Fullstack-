package com.example.RPG_Manager20.Controller;

import com.example.RPG_Manager20.Model.DTO.HabilidadeDTO;
import com.example.RPG_Manager20.Model.Entities.Habilidade;
import com.example.RPG_Manager20.Model.Mapper.HabilidadeMapper;
import com.example.RPG_Manager20.Service.HabilidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/habilidades")
@CrossOrigin(origins = "http://localhost:4200")  // ← PERMITE ANGULAR
public class HabilidadeController {
    @Autowired
    private HabilidadeService habilidadeService;

    @Autowired
    private HabilidadeMapper habilidadeMapper;

    public HabilidadeController(HabilidadeService habilidadeService) {

    }
    @PostMapping
    public ResponseEntity<HabilidadeDTO> crate(@RequestBody HabilidadeDTO habilidadeDTO){
        Habilidade habilidade = habilidadeMapper.toEntity(habilidadeDTO);
        habilidadeService.save(habilidade);// ← Veja o que chega
        return new ResponseEntity<>(habilidadeMapper.toDto(habilidade), HttpStatus.CREATED);
    }
    @GetMapping("/listarTodos")
    public List<HabilidadeDTO> listarTodos(){
        return habilidadeService.list().stream().map(u->habilidadeMapper.toDto(u)).collect(Collectors.toList());
    }
    @GetMapping("/listar/{idHabilidade}")
    public ResponseEntity<HabilidadeDTO> getHabilidade(@PathVariable("idHabilidade") Long idHabilidade){
        return new ResponseEntity<>(habilidadeMapper.toDto(habilidadeService.findById(idHabilidade)), HttpStatus.CREATED);
    }
    @PutMapping("/{idHabilidade}")
    public ResponseEntity<HabilidadeDTO> update(@RequestBody HabilidadeDTO habilidadeDTO,  @PathVariable("idHabilidade") Long idHabilidade){
        Habilidade habilidade = habilidadeMapper.toEntity(habilidadeDTO);
        Habilidade updated = habilidadeService.update(habilidadeDTO, idHabilidade);
        HabilidadeDTO updatedDTO = habilidadeMapper.toDto(updated);
        return new ResponseEntity<>(updatedDTO, HttpStatus.OK);
    }
}
