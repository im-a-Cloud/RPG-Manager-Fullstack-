package com.example.RPG_Manager20.Service;

import com.example.RPG_Manager20.Model.DTO.*;
import com.example.RPG_Manager20.Model.DTO.Request.PericiaPersonagemRequestDTO;
import com.example.RPG_Manager20.Model.DTO.Request.PersonagemRequestDTO;
import com.example.RPG_Manager20.Model.DTO.Response.PersonagemResponseDTO;
import com.example.RPG_Manager20.Model.Entities.*;
import com.example.RPG_Manager20.Model.Enums.ErrorMessageUtils;
import com.example.RPG_Manager20.Model.Mapper.PersonagemMapper;
import com.example.RPG_Manager20.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
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
    private ProficienciaRepository proficienciaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PericiaRepository periciaRepository;

    // ============================================
    // 1️⃣ CRIAR PERSONAGEM (COMPLETO)
    // ============================================
    @Transactional
    public PersonagemResponseDTO criarPersonagem(PersonagemRequestDTO requestDTO, Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

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
        personagem.setUsuario(usuario);

        // 3. Calcular valores automáticos
        calcularValoresAutomaticos(personagem);

        // 4. Salvar o personagem (primeiro para ter o ID)
        Personagem savedPersonagem = personagemRepository.save(personagem);
        System.out.println("✅ Personagem salvo com ID: " + savedPersonagem.getId());

        // 5. Sincronizar coleções filhas
        sincronizarPericias(savedPersonagem, requestDTO.pericias());
        sincronizarHabilidades(savedPersonagem, requestDTO.habilidades());
        sincronizarItens(savedPersonagem, requestDTO.inventario());
        sincronizarProficiencias(savedPersonagem, requestDTO.proficiencias());
        sincronizarMagias(savedPersonagem, requestDTO.magias());

        return PersonagemResponseDTO.from(savedPersonagem, classe, catalogo());
    }

    // ============================================
    // 2️⃣ BUSCAR PERSONAGEM POR ID
    // ============================================
    public PersonagemResponseDTO buscarPersonagemPorId(Long id) {
        Personagem personagem = findById(id);
        Classe classe = personagem.getClassePersonagem();
        return PersonagemResponseDTO.from(personagem, classe, catalogo());
    }

    // ============================================
    // 3️⃣ LISTAR
    // ============================================
    public List<PersonagemResponseDTO> listarTodos() {
        List<Pericia> cat = catalogo();
        return personagemRepository.findAll()
                .stream()
                .map(p -> PersonagemResponseDTO.from(p, p.getClassePersonagem(), cat))
                .toList();
    }

    public List<PersonagemResponseDTO> listarPorUsuario(Long usuarioId) {
        return personagemRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(p -> PersonagemResponseDTO.from(p, p.getClassePersonagem(), catalogo()))
                .toList();
    }

    // ============================================
    // 4️⃣ ATUALIZAR PERSONAGEM
    // ============================================
    @Transactional
    public PersonagemResponseDTO atualizarPersonagem(Long id, PersonagemRequestDTO requestDTO, Long idUsuario) {
        System.out.println("📤 ATUALIZANDO PERSONAGEM ID: " + id);

        Personagem personagemExistente = findById(id);

        // Verifica se pertence ao usuário
        if (personagemExistente.getUsuario() == null
                || !personagemExistente.getUsuario().getId().equals(idUsuario)) {
            throw new BusinessException(HttpStatus.FORBIDDEN, "Você não tem acesso a esse personagem");
        }

        Classe classe = personagemExistente.getClassePersonagem();

        // ============ CAMPOS BÁSICOS ============
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
        personagemExistente.setFotoBase64(requestDTO.fotoBase64());
        personagemExistente.setIdadePersonagem(requestDTO.idadePersonagem());
        personagemExistente.setGeneroPersonagem(requestDTO.generoPersonagem());
        personagemExistente.setAntecedentePersonagem(requestDTO.antecedentePersonagem());

        // ============ ATRIBUTOS ============
        personagemExistente.setValorForca(requestDTO.valorForca());
        personagemExistente.setValorDestreza(requestDTO.valorDestreza());
        personagemExistente.setValorConstituicao(requestDTO.valorConstituicao());
        personagemExistente.setValorInteligencia(requestDTO.valorInteligencia());
        personagemExistente.setValorSabedoria(requestDTO.valorSabedoria());
        personagemExistente.setValorCarisma(requestDTO.valorCarisma());

        // ============ CLASSE ============
        if (requestDTO.classeId() != null &&
                (classe == null || !classe.getId().equals(requestDTO.classeId()))) {
            classe = classeRepository.findById(requestDTO.classeId())
                    .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND,
                            "Classe não encontrada com ID: " + requestDTO.classeId()));
            personagemExistente.setClassePersonagem(classe);
        }

        // ============ COLEÇÕES FILHAS ============
        if (requestDTO.pericias() != null) {
            sincronizarPericias(personagemExistente, requestDTO.pericias());
        }
        if (requestDTO.habilidades() != null) {
            sincronizarHabilidades(personagemExistente, requestDTO.habilidades());
        }
        if (requestDTO.inventario() != null) {
            sincronizarItens(personagemExistente, requestDTO.inventario());
        }
        if (requestDTO.proficiencias() != null) {
            sincronizarProficiencias(personagemExistente, requestDTO.proficiencias());
        }
        if (requestDTO.magias() != null) {
            sincronizarMagias(personagemExistente, requestDTO.magias());
        }

        // ============ RECALCULAR ============
        calcularValoresAutomaticos(personagemExistente);

        // ============ SALVAR ============
        Personagem updated = personagemRepository.save(personagemExistente);
        System.out.println("✅ Personagem atualizado ID: " + updated.getId());

        return PersonagemResponseDTO.from(updated, classe, catalogo());
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
    // 8️⃣ MÉTODOS AUXILIARES — CÁLCULOS
    // ============================================
    private void calcularValoresAutomaticos(Personagem personagem) {
        int destrezaBonus = calcularBonusAtributo(personagem.getValorDestreza());
        personagem.setCaPersonagem(10 + destrezaBonus);
        personagem.setIniciativaPersonagem(destrezaBonus);
        personagem.setMovimentoPersonagem(9);

        int constBonus = calcularBonusAtributo(personagem.getValorConstituicao());
        int dadoVida = personagem.getClassePersonagem() != null
                ? personagem.getClassePersonagem().getDadoDeVida().getValor()
                : 6;
        personagem.setPontosVidaPersonagem(
                personagem.getNivelPersonagem() * dadoVida + constBonus
        );
    }

    private int calcularBonusAtributo(Integer valor) {
        if (valor == null) return 0;
        return Math.floorDiv(valor - 10, 2);
    }

    // ============================================
    // 9️⃣ SINCRONIZADORES (clear + recriar)
    // ============================================
    private void sincronizarPericias(Personagem personagem, List<PericiaPersonagemRequestDTO> dtos) {
        personagem.getPericiasPersonagem().clear();

        if (dtos == null || dtos.isEmpty()) {
            System.out.println("⚠️ sincronizarPericias: lista vazia");
            return;
        }

        for (PericiaPersonagemRequestDTO dto : dtos) {
            System.out.println("🔍 sincronizarPericias: slug=" + dto.slug()
                    + ", proficiente=" + dto.isProficiente());
            if (dto.slug() == null || dto.slug().isBlank()) continue;

            Pericia pericia = periciaRepository.findBySlug(dto.slug())
                    .orElseThrow(() -> new BusinessException(
                            HttpStatus.NOT_FOUND,
                            "Perícia não encontrada: " + dto.slug()));

            PersonagemPericia pp = new PersonagemPericia();
            pp.setPersonagem(personagem);
            pp.setPericia(pericia);
            pp.setProficiente(dto.isProficiente());
            personagem.getPericiasPersonagem().add(pp);
        }
    }

    private void sincronizarHabilidades(Personagem personagem, List<HabilidadeDTO> dtos) {
        personagem.getHabilidades().clear();

        if (dtos == null || dtos.isEmpty()) {
            return;
        }

        for (HabilidadeDTO dto : dtos) {
            Habilidade h = new Habilidade();
            h.setNomeHabilidade(dto.nomeHabilidade());
            h.setDescricaoHabilidade(dto.descricaoHabilidade());
            h.setOrigemHabilidade(dto.origemHabilidade());
            h.setUsosHabilidade(dto.usosHabilidade());
            h.setRecargaHabilidade(dto.recargaHabilidade());
            h.setPersonagem(personagem);
            personagem.getHabilidades().add(h);
        }
    }

    private void sincronizarItens(Personagem personagem, List<ItemDTO> dtos) {
        personagem.getInventarioPersonagem().clear();

        if (dtos == null || dtos.isEmpty()) {
            return;
        }

        for (ItemDTO dto : dtos) {
            Item item = new Item();
            item.setNomeItem(dto.nomeItem());
            item.setDescricaoItem(dto.descricaoItem());
            item.setPrecoItem(dto.precoItem());
            item.setRaridadeItem(dto.raridadeItem());
            item.setPesoItem(dto.pesoItem());
            item.setMagico(dto.isMagico());
            item.setPrecisaSintonizacao(dto.precisaSintonizacao());
            item.setQuantidade(dto.quantidade());
            item.setTipoItem(dto.tipoItem());
            item.setPersonagem(personagem);
            personagem.getInventarioPersonagem().add(item);
        }
    }

    private void sincronizarProficiencias(Personagem personagem, List<ProficienciaDTO> dtos) {
        personagem.getProficienciasPersonagem().clear();

        if (dtos == null || dtos.isEmpty()) {
            return;
        }

        for (ProficienciaDTO dto : dtos) {
            Proficiencia p = new Proficiencia();
            p.setTipoProficiencia(dto.tipoProficiencia());
            p.setListaProficiencias(dto.listaProficiencias());
            p.setDeleted(false);
            p.setPersonagem(personagem);
            personagem.getProficienciasPersonagem().add(p);
        }
    }

    private void sincronizarMagias(Personagem personagem, List<MagiaDTO> dtos) {
        personagem.getMagias().clear();

        if (dtos == null || dtos.isEmpty()) {
            return;
        }

        for (MagiaDTO dto : dtos) {
            Magia m = new Magia();
            m.setName(dto.name());
            m.setLevel(dto.level());
            m.setCasting_time(dto.casting_time());
            m.setRange(dto.range());
            m.setDescription(dto.description());
            m.setDuration(dto.duration());
            m.setSchool(dto.school());
            m.setRitual(dto.ritual());
            m.setConcentration(dto.concentration());
            m.setType(dto.type());
            m.setClasses(dto.classes());
            m.setTags(dto.tags());

            if (dto.components() != null) {
                Components c = new Components();
                c.setVerbal(dto.components().verbal());
                c.setSomatic(dto.components().somatic());
                c.setMaterial(dto.components().material());
                c.setRaw(dto.components().raw());
                m.setComponents(c);
            }

            m.setPersonagem(personagem);
            personagem.getMagias().add(m);
        }
    }
    private List<Pericia> catalogo() {
        return periciaRepository.findAllByOrderByNomeExibicaoAsc();
    }
    public PersonagemResponseDTO toResponseDto(Personagem personagem) {
        return PersonagemResponseDTO.from(
                personagem,
                personagem.getClassePersonagem(),
                catalogo()   // o catalogo() que já existe no PersonagemService
        );
    }
}