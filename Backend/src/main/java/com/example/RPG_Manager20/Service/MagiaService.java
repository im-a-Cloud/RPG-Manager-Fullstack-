package com.example.RPG_Manager20.Service;

import com.example.RPG_Manager20.Model.DTO.MagiaDTO;
import com.example.RPG_Manager20.Model.Entities.Classe;
import com.example.RPG_Manager20.Model.Entities.Magia;
import com.example.RPG_Manager20.Model.Entities.Personagem;
import com.example.RPG_Manager20.Model.Enums.Classes;
import com.example.RPG_Manager20.Model.Enums.ErrorMessageUtils;
import com.example.RPG_Manager20.Model.Enums.TipoConjuracao;
import com.example.RPG_Manager20.Repository.MagiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MagiaService {
    @Autowired
    MagiaRepository magiaRepository;

    public MagiaService(MagiaRepository magiaRepository){
        this.magiaRepository = magiaRepository;
    }
    public Magia save(Magia magia){
        return magiaRepository.save(magia);
    }
    public List<Magia> list(){
        return magiaRepository.findAll();
    }
    public void delete(Long idMagia){
        magiaRepository.deleteById(idMagia);
    }

    public Magia update(MagiaDTO magiaDTO, Long idMagia){
        Magia magiaAntiga = findById(idMagia);
        return magiaRepository.save(magiaAntiga);
    }

    public Magia findById(Long idMagia){
        Magia magia = magiaRepository.getById(idMagia);
        if (magia == null){
            throw new BusinessException(HttpStatus.NOT_FOUND, ErrorMessageUtils.ERROR_NOT_FOUND.getMessage("Magia"));
        }
        return magia;
    }

    private void validaNivelMagiaParaPersonagem(Personagem personagem, Magia magia){
        int nivelMagia = converterNivelMagia(magia.getLevel());
        int nivelPersonagem = personagem.getNivelPersonagem();
        TipoConjuracao tipoConjuracaoPersonagem = personagem.getClassePersonagem().getTipoConjuracao();

        if (tipoConjuracaoPersonagem == TipoConjuracao.NENHUM) {
            throw new BusinessException(HttpStatus.BAD_REQUEST,
                    String.format("A classe %s não é uma classe conjuradora",
                            personagem.getClassePersonagem().getNomeClasse()));
        }

        int nivelMaximoPermitido = calcularNivelMaximoMagia(nivelPersonagem, tipoConjuracaoPersonagem);

        if (nivelMaximoPermitido > nivelMagia) {
            throw new BusinessException(HttpStatus.BAD_REQUEST,
                    String.format("Personagem nível %d da classe %s só pode usar magias até nível %d. Magia %s é nível %d",
                            nivelPersonagem,
                            personagem.getClassePersonagem().getNomeClasse(),
                            nivelMaximoPermitido,
                            magia.getName(),
                            nivelMagia));
        }

        if (!classePodeUsarMagia(personagem.getClassePersonagem(), magia)) {
            throw new BusinessException(HttpStatus.BAD_REQUEST,
                    String.format("A classe %s não pode usar a magia %s",
                            personagem.getClassePersonagem().getNomeClasse(),
                            magia.getName()));
        }

        if (personagem.getMagias().contains(magia)) {
            throw new BusinessException(HttpStatus.BAD_REQUEST,
                    String.format("Personagem já conhece a magia %s", magia.getName()));
        }

        /*
        switch (personagem.getClassePersonagem().getTipoConjuracao()){
            case COMPLETO:
                if (nivelMagia <= (personagem.getNivelPersonagem()+1)/2){
                    if ()
                }
                break;
            case MEIO:
                if (personagem.getNivelPersonagem() < 2){
                    return;
                }
                if (nivelMagia <= (personagem.getNivelPersonagem()+3)/4){

                }
                break;
            case TERCIARIO:
                if (personagem.getNivelPersonagem() < 3){
                    return;
                }
                if (nivelMagia <= (personagem.getNivelPersonagem()+1)/3) {
                    throw new BusinessException(HttpStatus.BAD_REQUEST,
                            String.format("A classe %s não é uma classe conjuradora",
                                    personagem.getClassePersonagem().getNomeClasse()));
                }
                break;
            default:
                break;
        }

         */
    }
    private int converterNivelMagia(String levelString) {
        if (levelString == null) return 0;
        if (levelString.equalsIgnoreCase("cantrip") || levelString.equalsIgnoreCase("truque")) {
            return 0;
        }
        try {
            return Integer.parseInt(levelString);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    private boolean classePodeUsarMagia(Classe classe, Magia magia) {
        // Pega a tag da classe (ex: "wizard", "fighter", etc)
        String tagClasse = getTagByClasse(classe.getNomeClasse());
        if (tagClasse == null) return false;

        // Verifica se a tag está na lista de classes da magia
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
    private int calcularNivelMaximoMagia(int nivelPersonagem, TipoConjuracao tipo) {
        return switch (tipo) {
            case COMPLETO -> Math.min((nivelPersonagem + 1) / 2, 9);
            case MEIO -> {
                if (nivelPersonagem < 2) yield 0;
                yield Math.min((nivelPersonagem + 3) / 4, 5);
            }
            case TERCIARIO -> {
                if (nivelPersonagem < 3) yield 0;
                yield Math.min((nivelPersonagem + 1) / 3, 4);
            }
            default -> 0;
        };
    }

}

