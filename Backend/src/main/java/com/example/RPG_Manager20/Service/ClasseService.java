package com.example.RPG_Manager20.Service;

import com.example.RPG_Manager20.Model.DTO.ClasseDTO;
import com.example.RPG_Manager20.Model.Entities.Classe;
import com.example.RPG_Manager20.Model.Enums.ErrorMessageUtils;
import com.example.RPG_Manager20.Repository.ClasseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ClasseService {
    @Autowired
    private ClasseRepository classeRepository;

    public ClasseService(ClasseRepository classeRepository){
        this.classeRepository = classeRepository;
    }
    public Classe save(Classe classe){
        return classeRepository.save(classe);
    }
    public List<Classe> list(){
        return classeRepository.findAll();
    }
    public void delete(Long id){
        classeRepository.deleteById(id);
    }

    public Classe update(ClasseDTO classeDTO, Long idClasse){
        Classe classeAntigo = findById(idClasse);
        return classeRepository.save(classeAntigo);
    }

    public Classe findById(Long idClasse) {
        Classe classe = classeRepository.getById(idClasse);
        if (classe == null) {
            throw new BusinessException(HttpStatus.NOT_FOUND, ErrorMessageUtils.ERROR_NOT_FOUND.getMessage("Classe"));
        }
        return classe;
    }
}
