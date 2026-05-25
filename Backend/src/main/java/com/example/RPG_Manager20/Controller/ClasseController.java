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

    public ClasseController(ClasseService classeService) {

    }
    @PostMapping("/criar")
    public ResponseEntity<ClasseDTO> crate(@RequestBody ClasseDTO classeDTO){
        Classe classe = classeMapper.toEntity(classeDTO);
        classeService.save(classe);
        System.out.println("Recebido: " + classeDTO);
        return new ResponseEntity<>(classeMapper.toDto(classe), HttpStatus.CREATED);
    }
    @GetMapping("/listarTodos")
    public List<ClasseDTO> listarTodos(){
        return classeService.list().stream().map(u->classeMapper.toDto(u)).collect(Collectors.toList());
    }
    @GetMapping("/listar/{idClasse}")
    public ResponseEntity<ClasseDTO> getClasse(@PathVariable("idClasse") Long idClasse){
        return new ResponseEntity<>(classeMapper.toDto(classeService.findById(idClasse)), HttpStatus.CREATED);
    }
}
