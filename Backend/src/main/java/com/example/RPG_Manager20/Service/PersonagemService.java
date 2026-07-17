package com.example.RPG_Manager20.Service;

import com.example.RPG_Manager20.Model.DTO.PersonagemDTO;
import com.example.RPG_Manager20.Model.DTO.Request.PersonagemRequestDTO;
import com.example.RPG_Manager20.Model.DTO.Response.PersonagemResponseDTO;
import com.example.RPG_Manager20.Model.DTO.Summary.PersonagemSummaryDTO;
import com.example.RPG_Manager20.Model.Entities.Classe;
import com.example.RPG_Manager20.Model.Entities.Personagem;
import com.example.RPG_Manager20.Model.Enums.ErrorMessageUtils;
import com.example.RPG_Manager20.Model.Mapper.PersonagemMapper;
import com.example.RPG_Manager20.Repository.ClasseRepository;
import com.example.RPG_Manager20.Repository.PersonagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonagemService {

    @Autowired
    private PersonagemRepository personagemRepository;

    @Autowired
    private ClasseRepository classeRepository;

    @Autowired
    private PersonagemMapper personagemMapper;

    // Criar personagem
    public PersonagemResponseDTO criarPersonagem(PersonagemRequestDTO requestDTO) {
        // 1. Buscar a classe
        Classe classe = classeRepository.findById(requestDTO.classeId())
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND,
                        "Classe não encontrada com ID: " + requestDTO.classeId()));

        // 2. Converter Request → Entity
        Personagem personagem = personagemMapper.toEntity(requestDTO);
        personagem.setClassePersonagem(classe);

        personagem.setProficienciasPersonagem(new ArrayList<>(classe.getListaProficienciasClasse()));

        // 3. Salvar no banco
        personagem = personagemRepository.save(personagem);

        // 4. Converter Entity → Response (USA O CONSTRUTOR ESTÁTICO, NÃO O MAPPER)
        return PersonagemResponseDTO.from(personagem, classe);
    }

    // Buscar personagem por ID
    public PersonagemResponseDTO buscarPersonagemPorId(Long id) {
        Personagem personagem = findById(id);
        Classe classe = personagem.getClassePersonagem();
        // USA O CONSTRUTOR ESTÁTICO
        return PersonagemResponseDTO.from(personagem, classe);
    }

    // Listar todos (resumido para performance)
    public List<PersonagemResponseDTO> listarTodos() {
        return personagemRepository.findAll()
                .stream()
                .map(personagemMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // Atualizar personagem
    public PersonagemResponseDTO atualizarPersonagem(Long id, PersonagemRequestDTO requestDTO) {
        // 1. Buscar personagem existente
        Personagem personagemExistente = findById(id);

        //AJEITAR ISSO AQUI TÁ MAL OTIMIZADO PRA CARALHO(Deixar otimizações pro final)
        personagemExistente.setNomePersonagem(requestDTO.nomePersonagem());
        personagemExistente.setNivelPersonagem(requestDTO.nivelPersonagem());
        personagemExistente.setValorForca(requestDTO.valorForca());
        personagemExistente.setValorDestreza(requestDTO.valorDestreza());
        personagemExistente.setValorConstituicao(requestDTO.valorConstituicao());
        personagemExistente.setValorInteligencia(requestDTO.valorInteligencia());
        personagemExistente.setValorSabedoria(requestDTO.valorSabedoria());
        personagemExistente.setValorCarisma(requestDTO.valorCarisma());
        personagemExistente.setCaPersonagem(requestDTO.ca());
        personagemExistente.setIniciativaPersonagem(requestDTO.iniciativa());
        personagemExistente.setMovimentoPersonagem(requestDTO.movimento());
        personagemExistente.setPontosVidaPersonagem(requestDTO.pontosVida());

        // 3. Se a classe mudou, atualizar
        Classe classe = personagemExistente.getClassePersonagem();
        if (requestDTO.classeId() != null &&
                (classe == null || !classe.getId().equals(requestDTO.classeId()))) {
            classe = classeRepository.findById(requestDTO.classeId())
                    .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND,
                            "Classe não encontrada com ID: " + requestDTO.classeId()));
            personagemExistente.setClassePersonagem(classe);
        }

        // 4. Salvar atualização
        Personagem personagemAtualizado = personagemRepository.save(personagemExistente);

        // 5. Retornar response (USA O CONSTRUTOR ESTÁTICO)
        return PersonagemResponseDTO.from(personagemAtualizado, classe);
    }

    // Deletar personagem
    public void deletarPersonagem(Long id) {
        Personagem personagem = findById(id);
        personagemRepository.delete(personagem);
    }

    public Personagem save(Personagem personagem) {
        return personagemRepository.save(personagem);
    }

    public List<Personagem> list() {
        return personagemRepository.findAll();
    }

    public void delete(Long idPersonagem) {
        Personagem personagem = findById(idPersonagem);
        personagemRepository.delete(personagem);
    }

    public Personagem update(PersonagemDTO personagemAtualizado, Long idPersonagem) {
        Personagem personagemExistente = findById(idPersonagem);
        // Atualizar campos se necessário
        return personagemRepository.save(personagemExistente);
    }

    // Método base findById (usado internamente)
    public Personagem findById(Long idPersonagem) {
        return personagemRepository.findById(idPersonagem)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND,
                        ErrorMessageUtils.ERROR_NOT_FOUND.getMessage("Personagem")));
    }
}