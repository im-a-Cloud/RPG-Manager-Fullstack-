package com.example.RPG_Manager20.Service;

import com.example.RPG_Manager20.Model.DTO.PericiaDTO;
import com.example.RPG_Manager20.Model.Entities.Pericia;
import com.example.RPG_Manager20.Model.Enums.ErrorMessageUtils;
import com.example.RPG_Manager20.Repository.PericiaRepository;
import com.example.RPG_Manager20.Repository.PersonagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PericiaService {

    @Autowired
    private PericiaRepository periciaRepository;

    public PericiaService(PericiaRepository periciaRepository){
        this.periciaRepository = periciaRepository;
    }
    public Pericia save(Pericia pericia){
        return periciaRepository.save(pericia);
    }
    public List<Pericia> list(){
        return periciaRepository.findAll();
    }
    public void delete(Long id){
        periciaRepository.deleteById(id);
    }

    public Pericia update(PericiaDTO periciaDTO, Long idPericia){
        Pericia periciaAntigo = findById(idPericia);
        return periciaRepository.save(periciaAntigo);
    }

    public Pericia findById(Long idPericia) {
        Pericia pericia = periciaRepository.getById(idPericia);
        if (pericia == null) {
            throw new BusinessException(HttpStatus.NOT_FOUND, ErrorMessageUtils.ERROR_NOT_FOUND.getMessage("Pericia"));
        }
        return pericia;
    }
}
