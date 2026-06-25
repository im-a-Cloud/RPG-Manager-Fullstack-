package com.example.RPG_Manager20.Service;

import com.example.RPG_Manager20.Model.DTO.ClasseDTO;
import com.example.RPG_Manager20.Model.Entities.Classe;
import com.example.RPG_Manager20.Model.Entities.Proficiencia;
import com.example.RPG_Manager20.Model.Enums.ErrorMessageUtils;
import com.example.RPG_Manager20.Model.Mapper.ClasseMapper;
import com.example.RPG_Manager20.Model.Mapper.ProficienciaMapper;
import com.example.RPG_Manager20.Repository.ClasseRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClasseService {
    @Autowired
    private ClasseRepository classeRepository;

    @Autowired
    private ProficienciaMapper proficienciaMapper;

    @Autowired
    private ClasseMapper classeMapper;

    @Autowired
    private ProficienciaService proficienciaService;

    public ClasseService(ClasseRepository classeRepository){
        this.classeRepository = classeRepository;
    }
    @Transactional
    public Classe save(Classe classe){
        return classeRepository.save(classe);
    }
    @Transactional
    public ClasseDTO criarClasse(ClasseDTO classeDTO) {
        Classe classe = classeMapper.toEntity(classeDTO);
        if (classe.getListaProficienciasClasse() != null) {
            List<Proficiencia> proficienciasSalvas = classe.getListaProficienciasClasse()
                    .stream()
                    .map(proficienciaService::save)
                    .collect(Collectors.toList());
            classe.setListaProficienciasClasse(proficienciasSalvas);
        }
        Classe savedClasse = classeRepository.save(classe);

        // Converte de volta para DTO
        return classeMapper.toDto(savedClasse);
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
