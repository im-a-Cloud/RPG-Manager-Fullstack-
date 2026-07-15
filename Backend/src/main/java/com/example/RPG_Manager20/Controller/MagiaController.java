package com.example.RPG_Manager20.Controller;

import com.example.RPG_Manager20.Model.DTO.MagiaDTO;
import com.example.RPG_Manager20.Model.Entities.Magia;
import com.example.RPG_Manager20.Model.Mapper.MagiaMapper;
import com.example.RPG_Manager20.Service.MagiaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/magia")
@CrossOrigin(origins = "http://localhost:4200")  // ← PERMITE ANGULAR
public class MagiaController {

    @Autowired
    MagiaMapper magiaMapper;
    @Autowired
    MagiaService magiaService;

    public MagiaController(MagiaService magiaService) {
    }
    @PostMapping("/criar")
    public ResponseEntity<MagiaDTO> create(@Valid @RequestBody MagiaDTO magiaDTO){
        Magia magia = magiaMapper.toEntity(magiaDTO);
        magiaService.save(magia);
        return new ResponseEntity<>(magiaMapper.toDto(magia), HttpStatus.CREATED);

    }
    @GetMapping("/listar/{idMagia}")
    public ResponseEntity<MagiaDTO> getMagia(@PathVariable("idMagia") Long idMagia) {
        return new ResponseEntity<>(magiaMapper.toDto(magiaService.findById(idMagia)), HttpStatus.CREATED);
    }
    @GetMapping("/listarTodos")
    public List<MagiaDTO> listar() {
        return magiaService.list().stream().map(u-> magiaMapper.toDto(u)).collect(Collectors.toList());
    }
}
