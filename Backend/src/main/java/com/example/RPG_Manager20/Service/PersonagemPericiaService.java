package com.example.RPG_Manager20.Service;

import com.example.RPG_Manager20.Model.DTO.AdicionarPericiaRequestDTO;
import com.example.RPG_Manager20.Model.DTO.PersonagemDTO;
import com.example.RPG_Manager20.Model.DTO.Response.PericiaResponseDTO;
import com.example.RPG_Manager20.Model.DTO.Response.PersonagemResponseDTO;
import com.example.RPG_Manager20.Model.Entities.Classe;
import com.example.RPG_Manager20.Model.Entities.Pericia;
import com.example.RPG_Manager20.Model.Entities.Personagem;
import com.example.RPG_Manager20.Model.Entities.PersonagemPericia;
import com.example.RPG_Manager20.Repository.PericiaRepository;
import com.example.RPG_Manager20.Repository.PersonagemPericiaRepository;
import com.example.RPG_Manager20.Repository.PersonagemRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonagemPericiaService {

    @Autowired
    private PersonagemPericiaRepository personagemPericiaRepository;

    @Autowired
    private PersonagemService personagemService;

    @Autowired
    PersonagemRepository personagemRepository;

    @Autowired
    PericiaRepository periciaRepository;

    @Transactional
    public List<PericiaResponseDTO> listarPericiaPersonagem(Long idPersonagem){
        Personagem personagem = personagemService.findById(idPersonagem);
        Classe classe = personagem.getClassePersonagem();

        PersonagemResponseDTO personagemDTO = PersonagemResponseDTO.from(personagem, classe);

        List<PersonagemPericia> periciasPersonagem = personagemPericiaRepository.findByPersonagemId(idPersonagem);

        return periciasPersonagem.stream().map(pericias -> PericiaResponseDTO.from(pericias, personagemDTO)).collect(Collectors.toList());

    }

    @Transactional
    public PersonagemResponseDTO addPersonagemPericia(Long idPersonagem, AdicionarPericiaRequestDTO request) {
        Personagem personagem = personagemService.findById(idPersonagem);
        Pericia pericia = periciaRepository.findById(request.periciaId())
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND,
                        "Perícia não encontrada com ID: " + request.periciaId()));

        boolean jaTemPericia = personagemPericiaRepository.existsByPersonagemIdAndPericiaId(idPersonagem, pericia.getId());

        if (jaTemPericia) {
            throw new BusinessException(HttpStatus.BAD_REQUEST,
                    "Personagem já possui esta perícia");
        }

        PersonagemPericia personagemPericia = new PersonagemPericia(personagem,pericia, request.isProficiente());
        personagemPericiaRepository.save(personagemPericia);

        Classe classe = personagem.getClassePersonagem();
        return PersonagemResponseDTO.from(personagem, classe);

    }

    @Transactional
    public PersonagemResponseDTO atualizarProficiencia(Long personagemId, Long periciaId, boolean isProficiente) {
        Personagem personagem = personagemService.findById(personagemId);

        PersonagemPericia personagemPericia = personagemPericiaRepository
                .findByPersonagemIdAndPericiaId(personagemId, periciaId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND,
                        "Personagem não possui esta perícia"));

        personagemPericia.setProficiente(isProficiente);
        personagemPericiaRepository.save(personagemPericia);

        Classe classe = personagem.getClassePersonagem();
        return PersonagemResponseDTO.from(personagem, classe);
    }
}
