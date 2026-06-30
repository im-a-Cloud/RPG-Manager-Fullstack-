package com.example.RPG_Manager20.Service;

import com.example.RPG_Manager20.Model.DTO.AdicionarProficienciaRequestDTO;
import com.example.RPG_Manager20.Model.DTO.ProficienciaDTO;
import com.example.RPG_Manager20.Model.Entities.Personagem;
import com.example.RPG_Manager20.Model.Entities.Proficiencia;
import com.example.RPG_Manager20.Model.Mapper.PersonagemMapper;
import com.example.RPG_Manager20.Model.Mapper.ProficienciaMapper;
import com.example.RPG_Manager20.Repository.PersonagemRepository;
import com.example.RPG_Manager20.Repository.ProficienciaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonagemProficienciaService {

    @Autowired
    private PersonagemRepository personagemRepository;

    @Autowired
    private PersonagemMapper personagemMapper;

    @Autowired
    private PersonagemService personagemService;

    @Autowired
    private ProficienciaService proficienciaService;

    @Autowired
    private ProficienciaMapper proficienciaMapper;

    @Autowired
    private ProficienciaRepository proficienciaRepository;

    @Transactional
    public Personagem adicionarProficiencia(Long idPersonagem, AdicionarProficienciaRequestDTO request) {
        Personagem personagem = personagemService.findById(idPersonagem);
        Proficiencia novaProficiencia = new Proficiencia();
        novaProficiencia.setTipoProficiencia(request.tipoProficiencia());
        novaProficiencia.setListaProficiencias(request.listaProficiencias());

        Proficiencia proficiencia = proficienciaRepository.save(novaProficiencia);
        personagem.getProficienciasPersonagem().add(proficiencia);

        return personagemRepository.save(personagem);

    }

    @Transactional
    public Personagem atualizarProficiencia(Long idPersonagem, Long idProficiencia, ProficienciaDTO request) {
        Personagem personagem = personagemService.findById(idPersonagem);
        Proficiencia proficiencia = personagem.getProficienciasPersonagem().stream().filter
                (p -> p.getId().equals(idProficiencia)).findFirst().orElseThrow(()-> new RuntimeException("Proficiência não encontrada no personagem"));

        if (request.tipoProficiencia()!=null){
            proficiencia.setTipoProficiencia(request.tipoProficiencia());
        }
        if (request.listaProficiencias() != null && !request.listaProficiencias().isEmpty()) {
            proficiencia.setListaProficiencias(request.listaProficiencias());
        }
        return personagemRepository.save(personagem);
    }
    public List<Proficiencia> listaProficiencias(Long idPersonagem){
        Personagem personagem = personagemService.findById(idPersonagem);
        return  personagem.getProficienciasPersonagem();
    }
}
