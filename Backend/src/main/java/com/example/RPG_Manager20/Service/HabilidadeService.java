package com.example.RPG_Manager20.Service;

import com.example.RPG_Manager20.Model.DTO.HabilidadeDTO;
import com.example.RPG_Manager20.Model.Entities.Habilidade;
import com.example.RPG_Manager20.Model.Enums.ErrorMessageUtils;
import com.example.RPG_Manager20.Model.Mapper.HabilidadeMapper;
import com.example.RPG_Manager20.Repository.HabilidadeRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HabilidadeService {
    @Autowired
    private HabilidadeRepository habilidadeRepository;
    @Autowired
    private HabilidadeMapper habilidadeMapper;

    public HabilidadeService(HabilidadeRepository habilidadeRepository){
        this.habilidadeRepository = habilidadeRepository;
    }
    public Habilidade save(Habilidade habilidade){
        return habilidadeRepository.save(habilidade);
    }
    public List<Habilidade> list(){
        return habilidadeRepository.findAll();
    }
    public void delete(Long id){
        habilidadeRepository.deleteById(id);
    }

    public Habilidade update(HabilidadeDTO habilidadeDTO, Long idHabilidade){
        Habilidade habilidadeAntigo = findById(idHabilidade);
        habilidadeMapper.updateEntity(habilidadeAntigo, habilidadeDTO);
        return habilidadeRepository.save(habilidadeAntigo);
    }

    public Habilidade findById(Long idHabilidade) {
        Habilidade habilidade = habilidadeRepository.getById(idHabilidade);
        if (habilidade == null) {
            throw new BusinessException(HttpStatus.NOT_FOUND, ErrorMessageUtils.ERROR_NOT_FOUND.getMessage("Habilidade"));
        }
        return habilidade;
    }
}
