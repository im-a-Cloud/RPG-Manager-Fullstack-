package com.example.RPG_Manager20.Controller;

import com.example.RPG_Manager20.Model.DTO.MagiaDTO;
import com.example.RPG_Manager20.Model.DTO.PericiaDTO;
import com.example.RPG_Manager20.Model.DTO.PersonagemDTO;
import com.example.RPG_Manager20.Model.Entities.Pericia;
import com.example.RPG_Manager20.Model.Entities.Personagem;
import com.example.RPG_Manager20.Model.Mapper.PericiaMapper;
import com.example.RPG_Manager20.Service.PericiaService;
import com.example.RPG_Manager20.Service.PersonagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pericia")
public class PericiaController {
    @Autowired
    private PericiaService periciaService;

    @Autowired
    private PericiaMapper periciaMapper;

    public PericiaController(PericiaService periciaService) {

    }
    @PostMapping("/criar")
    public ResponseEntity<PericiaDTO> create(@RequestBody PericiaDTO periciaDTO) {
        Pericia pericia = periciaMapper.toEntity(periciaDTO);
        pericia = periciaService.save(pericia);
        return new ResponseEntity<>(periciaMapper.toDto(pericia), HttpStatus.CREATED);
    }

    @GetMapping("/listar/{idPericia}")
    public ResponseEntity<PericiaDTO> getMagia(@PathVariable("idPericia") Long idPericia) {
        return new ResponseEntity<>(periciaMapper.toDto(periciaService.findById(idPericia)), HttpStatus.CREATED);
    }
    @GetMapping("/listarTodos")
    public List<PericiaDTO> listar() {
        return periciaService.list().stream().map(u-> periciaMapper.toDto(u)).collect(Collectors.toList());
    }
}
