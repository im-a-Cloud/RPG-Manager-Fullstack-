package com.example.RPG_Manager20.Service;

import com.example.RPG_Manager20.Model.DTO.PersonagemDTO;
import com.example.RPG_Manager20.Model.DTO.Request.PersonagemRequestDTO;
import com.example.RPG_Manager20.Model.DTO.Response.PersonagemResponseDTO;
import com.example.RPG_Manager20.Model.Entities.Classe;
import com.example.RPG_Manager20.Model.Entities.Personagem;
import com.example.RPG_Manager20.Model.Enums.ErrorMessageUtils;
import com.example.RPG_Manager20.Model.Mapper.PersonagemMapper;
import com.example.RPG_Manager20.Repository.ClasseRepository;
import com.example.RPG_Manager20.Repository.PersonagemRepository;
import jakarta.transaction.Transactional;
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

    @Autowired
    private PersonagemPericiaService personagemPericiaService;

    // ============================================
    // 1️⃣ CRIAR PERSONAGEM (COMPLETO)
    // ============================================
    @Transactional
    public PersonagemResponseDTO criarPersonagem(PersonagemRequestDTO requestDTO) {
        System.out.println("========================================");
        System.out.println("📤 CRIANDO PERSONAGEM COMPLETO:");
        System.out.println("   Nome: " + requestDTO.nomePersonagem());
        System.out.println("   Raça: " + requestDTO.racaPersonagem());
        System.out.println("   Nível: " + requestDTO.nivelPersonagem());
        System.out.println("   Classe ID: " + requestDTO.classeId());
        System.out.println("   Habilidades: " + (requestDTO.habilidades() != null ? requestDTO.habilidades().size() : 0));
        System.out.println("   Itens: " + (requestDTO.inventario() != null ? requestDTO.inventario().size() : 0));
        System.out.println("   Magias: " + (requestDTO.magias() != null ? requestDTO.magias().size() : 0));
        System.out.println("   Perícias: " + (requestDTO.pericias() != null ? requestDTO.pericias().size() : 0));
        System.out.println("========================================");

        // 1. Buscar a classe
        Classe classe = classeRepository.findById(requestDTO.classeId())
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND,
                        "Classe não encontrada com ID: " + requestDTO.classeId()));

        // 2. Converter Request → Entity
        Personagem personagem = personagemMapper.toEntity(requestDTO);
        personagem.setClassePersonagem(classe);

        // 3. Copiar proficiências da classe
        if (classe.getListaProficienciasClasse() != null) {
            personagem.setProficienciasPersonagem(new ArrayList<>(classe.getListaProficienciasClasse()));
        }

        // 4. Calcular valores automáticos
        calcularValoresAutomaticos(personagem);

        // 5. Salvar o personagem (primeiro para ter o ID)
        Personagem savedPersonagem = personagemRepository.save(personagem);
        System.out.println("✅ Personagem salvo com ID: " + savedPersonagem.getId());

        // 🔥 6. ADICIONAR PERÍCIAS USANDO O SERVICE DEDICADO
        if (requestDTO.pericias() != null && !requestDTO.pericias().isEmpty()) {
            for (var periciaDTO : requestDTO.pericias()) {
                if (periciaDTO.pericia() != null && periciaDTO.pericia().id() != null) {
                    try {
                        personagemPericiaService.addPersonagemPericia(
                                savedPersonagem.getId(),
                                periciaDTO.pericia().id(),
                                periciaDTO.isProficiente()
                        );
                        System.out.println("   ✅ Perícia adicionada: " + periciaDTO.pericia().nomePericia());
                    } catch (Exception e) {
                        System.err.println("   ❌ Erro ao adicionar perícia: " + e.getMessage());
                    }
                }
            }
        }

        // 7. Buscar o personagem atualizado com as perícias
        Personagem finalPersonagem = personagemRepository.findById(savedPersonagem.getId())
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Personagem não encontrado"));

        // 8. Retornar Response
        return PersonagemResponseDTO.from(finalPersonagem, classe);
    }

    // ============================================
    // 2️⃣ BUSCAR PERSONAGEM POR ID
    // ============================================
    public PersonagemResponseDTO buscarPersonagemPorId(Long id) {
        Personagem personagem = findById(id);
        Classe classe = personagem.getClassePersonagem();
        return PersonagemResponseDTO.from(personagem, classe);
    }

    // ============================================
    // 3️⃣ LISTAR TODOS
    // ============================================
    public List<PersonagemResponseDTO> listarTodos() {
        return personagemRepository.findAll()
                .stream()
                .map(personagemMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // ============================================
    // 4️⃣ ATUALIZAR PERSONAGEM
    // ============================================
    @Transactional
    public PersonagemResponseDTO atualizarPersonagem(Long id, PersonagemRequestDTO requestDTO) {
        System.out.println("📤 ATUALIZANDO PERSONAGEM ID: " + id);

        Personagem personagemExistente = findById(id);
        Classe classe = personagemExistente.getClassePersonagem();

        // Atualizar campos básicos
        personagemExistente.setNomePersonagem(requestDTO.nomePersonagem());
        personagemExistente.setNivelPersonagem(requestDTO.nivelPersonagem());
        personagemExistente.setRacaPersonagem(requestDTO.racaPersonagem());
        personagemExistente.setHistoriaPersonagem(requestDTO.historiaPersonagem());
        personagemExistente.setAparenciaPersonagem(requestDTO.aparenciaPersonagem());
        personagemExistente.setIdeaisPersonagem(requestDTO.ideaisPersonagem());
        personagemExistente.setDefeitosPersonagem(requestDTO.defeitosPersonagem());
        personagemExistente.setAnotacoesPersonagem(requestDTO.anotacoesPersonagem());
        personagemExistente.setPersonalidadePersonagem(requestDTO.personalidadePersonagem());
        personagemExistente.setEscalaPersonagem(requestDTO.escalaPersonagem());
        personagemExistente.setAlinhamentoPersonagem(requestDTO.alinhamentoPersonagem());
        personagemExistente.setPesoPersonagem(requestDTO.pesoPersonagem());
        personagemExistente.setAlturaPersonagem(requestDTO.alturaPersonagem());

        // Atualizar atributos
        personagemExistente.setValorForca(requestDTO.valorForca());
        personagemExistente.setValorDestreza(requestDTO.valorDestreza());
        personagemExistente.setValorConstituicao(requestDTO.valorConstituicao());
        personagemExistente.setValorInteligencia(requestDTO.valorInteligencia());
        personagemExistente.setValorSabedoria(requestDTO.valorSabedoria());
        personagemExistente.setValorCarisma(requestDTO.valorCarisma());

        // Atualizar classe (se mudou)
        if (requestDTO.classeId() != null &&
                (classe == null || !classe.getId().equals(requestDTO.classeId()))) {
            classe = classeRepository.findById(requestDTO.classeId())
                    .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND,
                            "Classe não encontrada com ID: " + requestDTO.classeId()));
            personagemExistente.setClassePersonagem(classe);

            if (classe.getListaProficienciasClasse() != null) {
                personagemExistente.setProficienciasPersonagem(
                        new ArrayList<>(classe.getListaProficienciasClasse())
                );
            }
        }

        // Recalcular valores
        calcularValoresAutomaticos(personagemExistente);

        Personagem updated = personagemRepository.save(personagemExistente);
        System.out.println("✅ Personagem atualizado ID: " + updated.getId());

        return PersonagemResponseDTO.from(updated, classe);
    }

    // ============================================
    // 5️⃣ DELETAR PERSONAGEM
    // ============================================
    @Transactional
    public void deletarPersonagem(Long id) {
        Personagem personagem = findById(id);
        personagemRepository.delete(personagem);
        System.out.println("🗑️ Personagem deletado ID: " + id);
    }

    // ============================================
    // 6️⃣ MÉTODOS LEGADO
    // ============================================
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
        return personagemRepository.save(personagemExistente);
    }

    // ============================================
    // 7️⃣ MÉTODO BASE (findById)
    // ============================================
    public Personagem findById(Long idPersonagem) {
        return personagemRepository.findById(idPersonagem)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND,
                        ErrorMessageUtils.ERROR_NOT_FOUND.getMessage("Personagem")));
    }

    // ============================================
    // 8️⃣ MÉTODOS AUXILIARES
    // ============================================
    private void calcularValoresAutomaticos(Personagem personagem) {
        int destrezaBonus = calcularBonusAtributo(personagem.getValorDestreza());
        personagem.setCaPersonagem(10 + destrezaBonus);
        personagem.setIniciativaPersonagem(destrezaBonus);
        personagem.setMovimentoPersonagem(9);

        int constBonus = calcularBonusAtributo(personagem.getValorConstituicao());
        int dadoVida = personagem.getClassePersonagem() != null ?
                personagem.getClassePersonagem().getDadoDeVida().getValor() : 6;
        personagem.setPontosVidaPersonagem(
                personagem.getNivelPersonagem() * dadoVida + constBonus
        );
    }

    private int calcularBonusAtributo(Integer valor) {
        if (valor == null) return 0;
        return (valor - 10) / 2;
    }
}