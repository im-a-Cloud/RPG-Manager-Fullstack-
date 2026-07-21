package com.example.RPG_Manager20.Service;

import com.example.RPG_Manager20.Model.DTO.AdicionarPericiaRequestDTO;
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
    private PersonagemRepository personagemRepository;  // ← DIRETO, SEM PERSONAGEMSERVICE!

    @Autowired
    private PericiaRepository periciaRepository;

    // ============================================
    // FIND BY ID (DIRETO)
    // ============================================
    private Personagem findPersonagemById(Long id) {
        return personagemRepository.findById(id)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND,
                        "Personagem não encontrado com ID: " + id));
    }

    @Transactional
    public List<PericiaResponseDTO> listarPericiaPersonagem(Long idPersonagem) {
        Personagem personagem = findPersonagemById(idPersonagem);
        Classe classe = personagem.getClassePersonagem();

        PersonagemResponseDTO personagemDTO = PersonagemResponseDTO.from(personagem, classe);

        List<PersonagemPericia> periciasPersonagem = personagemPericiaRepository.findByPersonagemId(idPersonagem);

        return periciasPersonagem.stream()
                .map(pericias -> PericiaResponseDTO.from(pericias, personagemDTO))
                .collect(Collectors.toList());
    }

    @Transactional
    public PersonagemResponseDTO addPersonagemPericia(Long idPersonagem, Long periciaId, Boolean isProficiente) {
        Personagem personagem = findPersonagemById(idPersonagem);
        Pericia pericia = periciaRepository.findById(periciaId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND,
                        "Perícia não encontrada com ID: " + periciaId));

        boolean jaTemPericia = personagemPericiaRepository.existsByPersonagemIdAndPericiaId(idPersonagem, periciaId);

        if (jaTemPericia) {
            throw new BusinessException(HttpStatus.BAD_REQUEST,
                    "Personagem já possui esta perícia");
        }

        PersonagemPericia personagemPericia = new PersonagemPericia();
        personagemPericia.setPersonagem(personagem);
        personagemPericia.setPericia(pericia);
        personagemPericia.setProficiente(isProficiente != null && isProficiente);

        personagemPericiaRepository.save(personagemPericia);

        Classe classe = personagem.getClassePersonagem();
        return PersonagemResponseDTO.from(personagem, classe);
    }

    @Transactional
    public PersonagemResponseDTO atualizarProficiencia(Long personagemId, Long periciaId, boolean isProficiente) {
        Personagem personagem = findPersonagemById(personagemId);

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