package com.example.RPG_Manager20.Service;

import com.example.RPG_Manager20.Model.DTO.ClasseDTO;
import com.example.RPG_Manager20.Model.Entities.Classe;
import com.example.RPG_Manager20.Model.Entities.Proficiencia;
import com.example.RPG_Manager20.Model.Enums.ErrorMessageUtils;
import com.example.RPG_Manager20.Model.Mapper.ClasseMapper;
import com.example.RPG_Manager20.Model.Mapper.ProficienciaMapper;
import com.example.RPG_Manager20.Repository.ClasseRepository;
import com.example.RPG_Manager20.Repository.ProficienciaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClasseService {

    @Autowired
    private ClasseRepository classeRepository;

    @Autowired
    private ClasseMapper classeMapper;

    @Autowired
    private ProficienciaService proficienciaService;

    @Autowired
    private ProficienciaRepository proficienciaRepository;

    // ============================================
    // CRIAÇÃO
    // ============================================
    @Transactional
    public ClasseDTO criarClasse(ClasseDTO classeDTO) {
        // Converter DTO → Entity

        System.out.println("========================================");
        System.out.println("📤 DTO RECEBIDO:");
        System.out.println("   isConjurador: " + classeDTO.isConjurador());
        System.out.println("========================================");

        Classe classe = classeMapper.toEntity(classeDTO);

        System.out.println("🔍 ENTITY APÓS MAPPER:");
        System.out.println("   conjurador: " + classe.isConjurador());
        System.out.println("========================================");

        classe.setConjurador(classeDTO.isConjurador());

        System.out.println("🔍 ENTITY APÓS FORÇAR:");
        System.out.println("   conjurador: " + classe.isConjurador());
        System.out.println("========================================");

        // 🔥 PROCESSAR PROFICIÊNCIAS CORRETAMENTE
        if (classe.getListaProficienciasClasse() != null && !classe.getListaProficienciasClasse().isEmpty()) {
            List<Proficiencia> proficienciasSalvas = new ArrayList<>();

            for (Proficiencia proficiencia : classe.getListaProficienciasClasse()) {
                if (proficiencia.getId() != null) {
                    // 🔥 JÁ EXISTE - BUSCAR DO BANCO
                    Proficiencia existente = proficienciaRepository.findById(proficiencia.getId())
                            .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND,
                                    "Proficiência não encontrada com ID: " + proficiencia.getId()));
                    proficienciasSalvas.add(existente);
                } else {
                    // 🔥 NOVA - SALVAR
                    Proficiencia nova = proficienciaRepository.save(proficiencia);
                    proficienciasSalvas.add(nova);
                }
            }

            classe.setListaProficienciasClasse(proficienciasSalvas);
        }

        // Salvar a classe
        Classe savedClasse = classeRepository.save(classe);

        System.out.println("✅ CLASSE SALVA:");
        System.out.println("   conjurador: " + savedClasse.isConjurador());
        System.out.println("========================================");

        ClasseDTO response = classeMapper.toDto(savedClasse);


        System.out.println("📤 RESPONSE DTO:");
        System.out.println("   isConjurador: " + response.isConjurador());
        System.out.println("========================================");


        return classeMapper.toDto(savedClasse);
    }

    // ============================================
    // LISTAR TODAS - RETORNA DTO
    // ============================================
    public List<ClasseDTO> listarTodas() {
        List<Classe> classes = classeRepository.findAll();

        System.out.println("📋 Encontradas " + classes.size() + " classes");

        return classes.stream()
                .map(classe -> {
                    ClasseDTO dto = classeMapper.toDto(classe);

                    // 🔥 LOG DO ID
                    System.out.println("   Classe: " + classe.getNomeClasse() +
                            ", ID: " + classe.getId() +
                            ", DTO ID: " + dto.getId());

                    return dto;
                })
                .collect(Collectors.toList());
    }

    // ============================================
    // BUSCAR POR ID - RETORNA DTO
    // ============================================
    public ClasseDTO buscarPorId(Long idClasse) {
        Classe classe = findById(idClasse);
        return classeMapper.toDto(classe);
    }

    // ============================================
    // BUSCAR POR ID - RETORNA ENTITY (INTERNO)
    // ============================================
    public Classe findById(Long idClasse) {
        return classeRepository.findById(idClasse)
                .orElseThrow(() -> new BusinessException(
                        HttpStatus.NOT_FOUND,
                        ErrorMessageUtils.ERROR_NOT_FOUND.getMessage("Classe")
                ));
    }

    @Transactional
    public void deletarClasse(Long idClasse) {
        if (!classeRepository.existsById(idClasse)) {
            throw new BusinessException(
                    HttpStatus.NOT_FOUND,
                    ErrorMessageUtils.ERROR_NOT_FOUND.getMessage("Classe")
            );
        }
        classeRepository.deleteById(idClasse);
    }

    // ============================================
    // MÉTODOS LEGADO (MANTIDOS PARA COMPATIBILIDADE)
    // ============================================
    @Transactional
    public Classe save(Classe classe) {
        return classeRepository.save(classe);
    }

    public List<Classe> list() {
        return classeRepository.findAll();
    }

    public void delete(Long id) {
        classeRepository.deleteById(id);
    }
}