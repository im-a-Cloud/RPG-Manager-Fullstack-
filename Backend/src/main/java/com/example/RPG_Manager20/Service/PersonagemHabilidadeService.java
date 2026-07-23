package com.example.RPG_Manager20.Service;

import com.example.RPG_Manager20.Model.DTO.HabilidadeDTO;
import com.example.RPG_Manager20.Model.DTO.Response.HabilidadeResponseDTO;
import com.example.RPG_Manager20.Model.DTO.Response.PersonagemResponseDTO;
import com.example.RPG_Manager20.Model.Entities.Classe;
import com.example.RPG_Manager20.Model.Entities.Habilidade;
import com.example.RPG_Manager20.Model.Entities.Personagem;
import com.example.RPG_Manager20.Model.Mapper.HabilidadeMapper;
import com.example.RPG_Manager20.Model.Mapper.PersonagemMapper;
import com.example.RPG_Manager20.Repository.HabilidadeRepository;
import com.example.RPG_Manager20.Repository.PersonagemRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonagemHabilidadeService {

    @Autowired
    private HabilidadeService habilidadeService;

    @Autowired
    private HabilidadeMapper habilidadeMapper;

    @Autowired
    private HabilidadeRepository habilidadeRepository;

    @Autowired
    private PersonagemMapper personagemMapper;

    @Autowired
    private PersonagemService personagemService;

    @Autowired
    private PersonagemRepository personagemRepository;

    private Habilidade buscarHabilidade(Long idHabilidade) {
        return habilidadeRepository.findById(idHabilidade)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND,
                        "Habilidade não encontrada com ID: " + idHabilidade));
    }

    @Transactional
    //adicionar uma habiliade que já existe no banco
    //provavelmente será removido por não haver um JSON com todos os features
    public PersonagemResponseDTO adicionarHabilidade(Long idPersonagem, Long idHabilidade) {
        Personagem personagem = personagemService.findById(idPersonagem);
        Habilidade habilidade = habilidadeService.findById(idHabilidade);

        if (personagem.getHabilidades().contains(habilidade)) {
            throw new BusinessException(HttpStatus.BAD_REQUEST,
                    "Personagem já possui esta habilidade");
        }

        personagem.getHabilidades().add(habilidade);
        personagem = personagemRepository.save(personagem);

        return PersonagemResponseDTO.from(personagem, personagem.getClassePersonagem());
    }

    @Transactional
    public PersonagemResponseDTO removerHabilidade(Long personagemId, Long idHabilidade) {
        Personagem personagem = personagemService.findById(personagemId);
        Habilidade habilidade = buscarHabilidade(idHabilidade);

        if (!personagem.getHabilidades().contains(habilidade)) {
            throw new BusinessException(HttpStatus.BAD_REQUEST,
                    "Personagem não possui esta habilidade");
        }

        personagem.getHabilidades().remove(habilidade);
        personagem = personagemRepository.save(personagem);

        // Retorna o personagem atualizado (consistente com adicionarHabilidade)
        return PersonagemResponseDTO.from(personagem, personagem.getClassePersonagem());
    }

    public List<HabilidadeResponseDTO> listarHabilidadesDoPersonagem(Long personagemId) {
        Personagem personagem = personagemService.findById(personagemId);

        return personagem.getHabilidades().stream()
                .map(habilidadeMapper::toResponseDto)
                .collect(Collectors.toList());
    }
    //deixar isso aqui como exclusivo de ADM
    //Atualiza a habildiade de forma geral
    public HabilidadeResponseDTO atualizarHabilidade(Long id, HabilidadeDTO dto) {
        Habilidade habilidade = habilidadeRepository.findById(id)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND,
                        "Habilidade não encontrada com ID: " + id));

        habilidadeMapper.updateEntity(habilidade, dto);
        habilidade = habilidadeRepository.save(habilidade);
        return habilidadeMapper.toResponseDto(habilidade);
    }
}