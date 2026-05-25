package com.example.RPG_Manager20.Service;

import com.example.RPG_Manager20.Model.DTO.MagiaDTO;
import com.example.RPG_Manager20.Model.Entities.Classe;
import com.example.RPG_Manager20.Model.Entities.Magia;
import com.example.RPG_Manager20.Model.Entities.Personagem;
import com.example.RPG_Manager20.Model.Enums.Classes;
import com.example.RPG_Manager20.Model.Enums.TipoConjuracao;
import com.example.RPG_Manager20.Model.Mapper.MagiaMapper;
import com.example.RPG_Manager20.Model.Mapper.PersonagemMapper;
import com.example.RPG_Manager20.Repository.MagiaRepository;
import com.example.RPG_Manager20.Repository.PersonagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonagemMagiaService {

    @Autowired
    private PersonagemRepository personagemRepository;

    @Autowired
    private MagiaRepository magiaRepository;

    @Autowired
    private PersonagemService personagemService;

    @Autowired
    private PersonagemMapper personagemMapper;  // ← Adicionado

    @Autowired
    private MagiaMapper magiaMapper;  // ← Adicionado (se tiver)

    @Transactional
    public Personagem adicionarMagia(Long personagemId, Long magiaId) {
        Personagem personagem = personagemService.findById(personagemId);
        Magia magia = magiaRepository.findById(magiaId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND,
                        "Magia não encontrada com ID: " + magiaId));

        validarMagiaParaPersonagem(personagem, magia);

        personagem.getMagias().add(magia);
        return personagemRepository.save(personagem);
    }

    @Transactional
    public Personagem removerMagia(Long personagemId, Long magiaId) {
        Personagem personagem = personagemService.findById(personagemId);
        Magia magia = magiaRepository.findById(magiaId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND,
                        "Magia não encontrada com ID: " + magiaId));

        if (!personagem.getMagias().contains(magia)) {
            throw new BusinessException(HttpStatus.BAD_REQUEST,
                    "Personagem não possui esta magia");
        }

        personagem.getMagias().remove(magia);
        return personagemRepository.save(personagem);
    }

    public List<Magia> listarMagiasDoPersonagem(Long personagemId) {
        Personagem personagem = personagemService.findById(personagemId);
        return personagem.getMagias();
    }


    //MÉTODOS DE VALIDAÇÃO

    private void validarMagiaParaPersonagem(Personagem personagem, Magia magia) {
        int nivelMagia = converterNivelMagia(magia.getLevel());
        int nivelPersonagem = personagem.getNivelPersonagem();
        TipoConjuracao tipo = personagem.getClassePersonagem().getTipoConjuracao();

        // Verificar se a classe pode usar magias
        if (tipo == null || tipo == TipoConjuracao.NENHUM) {
            throw new BusinessException(HttpStatus.BAD_REQUEST,
                    String.format("A classe %s não é uma classe conjuradora",
                            personagem.getClassePersonagem().getNomeClasse()));
        }

        //Verificar nível mínimo da magia
        int nivelMaximoPermitido = calcularNivelMaximoMagia(nivelPersonagem, tipo);

        if (nivelMagia > nivelMaximoPermitido) {
            throw new BusinessException(HttpStatus.BAD_REQUEST,
                    String.format("Personagem nível %d da classe %s só pode usar magias até nível %d. Magia %s é nível %d",
                            nivelPersonagem,
                            personagem.getClassePersonagem().getNomeClasse(),
                            nivelMaximoPermitido,
                            magia.getName(),
                            nivelMagia));
        }

        //Verificar se a classe específica pode usar a magia
        if (!classePodeUsarMagia(personagem.getClassePersonagem(), magia)) {
            throw new BusinessException(HttpStatus.BAD_REQUEST,
                    String.format("A classe %s não pode usar a magia %s",
                            personagem.getClassePersonagem().getNomeClasse(),
                            magia.getName()));
        }

        //Verificar se já possui a magia
        if (personagem.getMagias().contains(magia)) {
            throw new BusinessException(HttpStatus.BAD_REQUEST,
                    String.format("Personagem já conhece a magia %s", magia.getName()));
        }
    }

    private int calcularNivelMaximoMagia(int nivelPersonagem, TipoConjuracao tipo) {
        return switch (tipo) {
            case COMPLETO -> Math.min((nivelPersonagem + 1) / 2, 9);
            case MEIO -> (nivelPersonagem < 2) ? 0 : Math.min((nivelPersonagem + 3) / 4, 5);
            case TERCIARIO -> (nivelPersonagem < 3) ? 0 : Math.min((nivelPersonagem + 1) / 3, 4);
            default -> 0;
        };
    }

    private int converterNivelMagia(String levelString) {
        if (levelString == null) return 0;
        if (levelString.equalsIgnoreCase("cantrip") || levelString.equalsIgnoreCase("truque") || levelString.equals("0")) {
            return 0;
        }
        try {
            return Integer.parseInt(levelString);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private boolean classePodeUsarMagia(Classe classe, Magia magia) {
        String tagClasse = getTagByClasse(classe.getNomeClasse());
        if (tagClasse == null) return false;
        return magia.getClasses() != null && magia.getClasses().contains(tagClasse);
    }

    private String getTagByClasse(Classes classe) {
        return switch (classe) {
            case BARBARO -> "barbarian";
            case BARDO -> "bard";
            case BRUXO -> "warlock";
            case CLERIGO -> "cleric";
            case DRUIDA -> "druid";
            case FEITICEIRO -> "sorcerer";
            case GUERREIRO -> "fighter";
            case LADINO -> "rogue";
            case MAGO -> "wizard";
            case PALADINO -> "paladin";
            case RANGER -> "ranger";
            case ARTIFICIE -> "artificer";
        };

    }

}