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
        if (proficiencia.getTipoProficiencia() == null){
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Tipo de proficiência é obrigatório");
        }
        if (proficiencia.getListaProficiencias() == null || proficiencia.getListaProficiencias().isEmpty()){}
        return proficienciaRepository.save(proficiencia);
    }
    public List<Proficiencia> list(){
        return proficienciaRepository.findAll();
    }
    public Proficiencia findById(Long idProficiencia) {
        Proficiencia proficiencia = proficienciaRepository.getById(idProficiencia);
        if (proficiencia == null) {
            throw new BusinessException(HttpStatus.NOT_FOUND, ErrorMessageUtils.ERROR_NOT_FOUND.getMessage("Proficiencia", idProficiencia));
        }
        return proficiencia;
    }
    public Proficiencia update(ProficienciaDTO proficienciaDTO, Long idProficiencia) {
        Proficiencia proficienciaAntigo = findById(idProficiencia);
        proficienciaMapper.updateEntity(proficienciaAntigo, proficienciaDTO);
        return proficienciaRepository.save(proficienciaAntigo);
    }
}
