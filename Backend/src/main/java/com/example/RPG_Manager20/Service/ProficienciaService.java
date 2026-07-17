package com.example.RPG_Manager20.Service;

import com.example.RPG_Manager20.Model.DTO.ProficienciaDTO;
import com.example.RPG_Manager20.Model.Entities.Proficiencia;
import com.example.RPG_Manager20.Model.Entities.Proficiencia;
import com.example.RPG_Manager20.Model.Enums.ErrorMessageUtils;
import com.example.RPG_Manager20.Model.Mapper.ProficienciaMapper;
import com.example.RPG_Manager20.Repository.ProficienciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProficienciaService {
    @Autowired
    private ProficienciaMapper proficienciaMapper;
    @Autowired
    private ProficienciaRepository proficienciaRepository;

    public ProficienciaService(ProficienciaRepository proficienciaRepository) {
        this.proficienciaRepository = proficienciaRepository;
    }
    public Proficiencia save(Proficiencia proficiencia){
        return proficienciaRepository.save(proficiencia);
    }
    public List<Proficiencia> list(){
        return proficienciaRepository.findAll();
    }
    public Proficiencia findById(Long idProficiencia) {
        Proficiencia pericia = proficienciaRepository.getById(idProficiencia);
        if (pericia == null) {
            throw new BusinessException(HttpStatus.NOT_FOUND, ErrorMessageUtils.ERROR_NOT_FOUND.getMessage("Proficiencia", idProficiencia));
        }
        return pericia;
    }
    public Proficiencia update(ProficienciaDTO proficienciaDTO, Long idProficiencia) {
        Proficiencia proficienciaAntigo = findById(idProficiencia);
        proficienciaMapper.updateEntity(proficienciaAntigo, proficienciaDTO);
        return proficienciaRepository.save(proficienciaAntigo);
    }
}
