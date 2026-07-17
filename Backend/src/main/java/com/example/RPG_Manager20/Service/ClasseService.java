package com.example.RPG_Manager20.Service;

import com.example.RPG_Manager20.Model.DTO.ClasseDTO;
import com.example.RPG_Manager20.Model.Entities.Classe;
import com.example.RPG_Manager20.Model.Entities.Proficiencia;
import com.example.RPG_Manager20.Model.Enums.ErrorMessageUtils;
import com.example.RPG_Manager20.Model.Mapper.ClasseMapper;
import com.example.RPG_Manager20.Model.Mapper.ProficienciaMapper;
import com.example.RPG_Manager20.Repository.ClasseRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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

    // ============================================
    // CRIAÇÃO
    // ============================================
    @Transactional
    public ClasseDTO criarClasse(ClasseDTO classeDTO) {
        // Converte DTO para Entity
        Classe classe = classeMapper.toEntity(classeDTO);

        // Salva as proficiências se existirem
        if (classe.getListaProficienciasClasse() != null && !classe.getListaProficienciasClasse().isEmpty()) {
            List<Proficiencia> proficienciasSalvas = classe.getListaProficienciasClasse()
                    .stream()
                    .map(proficiencia -> {
                        // Verifica se a proficiência já existe
                        if (proficiencia.getId() != null) {
                            return proficiencia;
                        }
                        return proficienciaService.save(proficiencia);
                    })
                    .collect(Collectors.toList());
            classe.setListaProficienciasClasse(proficienciasSalvas);
        }

        // Salva a classe
        Classe savedClasse = classeRepository.save(classe);

        // Retorna DTO
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