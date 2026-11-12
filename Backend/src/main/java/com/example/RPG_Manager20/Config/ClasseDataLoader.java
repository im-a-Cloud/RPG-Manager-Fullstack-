package com.example.RPG_Manager20.Config;

import com.example.RPG_Manager20.Model.Entities.Classe;
import com.example.RPG_Manager20.Model.Entities.Proficiencia;
import com.example.RPG_Manager20.Model.Enums.*;
import com.example.RPG_Manager20.Repository.ClasseRepository;
import com.example.RPG_Manager20.Repository.ProficienciaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class ClasseDataLoader implements CommandLineRunner {

    private final ClasseRepository classeRepository;
    private final ProficienciaRepository proficienciaRepository;

    public ClasseDataLoader(ClasseRepository classeRepository,
                            ProficienciaRepository proficienciaRepository) {
        this.classeRepository = classeRepository;
        this.proficienciaRepository = proficienciaRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (classeRepository.count() > 0) {
            return;
        }

        List<Classe> classes = List.of(
                barbaro(),
                bardo(),
                bruxo(),
                clerigo(),
                druida(),
                feiticeiro(),
                guerreiro(),
                ladino(),
                mago(),
                monge(),
                paladino(),
                patrulheiro()
        );

        classeRepository.saveAll(classes);
    }

    // ============================================
    // HELPERS
    // ============================================
    private Proficiencia criarProficiencia(Classe classe, TipoProficiencia tipo, String lista) {
        Proficiencia p = new Proficiencia();
        p.setTipoProficiencia(tipo);
        p.setListaProficiencias(lista);
        p.setDeleted(false);
        return p;
    }

    private Classe novaClasse(Classes nome, DadosDeVida dado,
                              boolean conjurador, Atributos attrConj, TipoConjuracao tipoConj,
                              List<Atributos> salvaguardas) {
        Classe c = new Classe();
        c.setNomeClasse(nome);
        c.setDadoDeVida(dado);
        c.setConjurador(conjurador);
        c.setAtributoConjuracao(attrConj);
        c.setTipoConjuracao(tipoConj);
        c.setProficienciaSalvaguarda(salvaguardas);
        c.setDeleted(false);
        return c;
    }

    // ============================================
    // CLASSES
    // ============================================

    private Classe barbaro() {
        Classe c = novaClasse(Classes.BARBARO, DadosDeVida.D12, false, null, TipoConjuracao.NENHUM,
                List.of(Atributos.FORCA, Atributos.CONSTITUICAO));
        List<Proficiencia> profs = new ArrayList<>();
        profs.add(criarProficiencia(c, TipoProficiencia.ARMAS,
                "Armas simples, armas marciais"));
        profs.add(criarProficiencia(c, TipoProficiencia.ARMADURAS,
                "Armaduras leves, armaduras médias, escudos"));
        c.setListaProficienciasClasse(profs);
        return c;
    }

    private Classe bardo() {
        Classe c = novaClasse(Classes.BARDO, DadosDeVida.D8, true, Atributos.CARISMA, TipoConjuracao.COMPLETO,
                List.of(Atributos.DESTREZA, Atributos.CARISMA));
        List<Proficiencia> profs = new ArrayList<>();
        profs.add(criarProficiencia(c, TipoProficiencia.ARMAS,
                "Armas simples, bestas de mão, espadas longas, rapieiras, espadas curtas"));
        profs.add(criarProficiencia(c, TipoProficiencia.ARMADURAS, "Armaduras leves"));
        profs.add(criarProficiencia(c, TipoProficiencia.FERRAMENTAS,
                "Três instrumentos musicais à escolha"));
        c.setListaProficienciasClasse(profs);
        return c;
    }

    private Classe bruxo() {
        Classe c = novaClasse(Classes.BRUXO, DadosDeVida.D8, true, Atributos.CARISMA, TipoConjuracao.COMPLETO,
                List.of(Atributos.SABEDORIA, Atributos.CARISMA));
        List<Proficiencia> profs = new ArrayList<>();
        profs.add(criarProficiencia(c, TipoProficiencia.ARMAS, "Armas simples"));
        profs.add(criarProficiencia(c, TipoProficiencia.ARMADURAS, "Armaduras leves"));
        c.setListaProficienciasClasse(profs);
        return c;
    }

    private Classe clerigo() {
        Classe c = novaClasse(Classes.CLERIGO, DadosDeVida.D8, true, Atributos.SABEDORIA, TipoConjuracao.COMPLETO,
                List.of(Atributos.SABEDORIA, Atributos.CARISMA));
        List<Proficiencia> profs = new ArrayList<>();
        profs.add(criarProficiencia(c, TipoProficiencia.ARMAS, "Armas simples"));
        profs.add(criarProficiencia(c, TipoProficiencia.ARMADURAS,
                "Armaduras leves, armaduras médias, escudos"));
        c.setListaProficienciasClasse(profs);
        return c;
    }

    private Classe druida() {
        Classe c = novaClasse(Classes.DRUIDA, DadosDeVida.D8, true, Atributos.SABEDORIA, TipoConjuracao.COMPLETO,
                List.of(Atributos.INTELIGENCIA, Atributos.SABEDORIA));
        List<Proficiencia> profs = new ArrayList<>();
        profs.add(criarProficiencia(c, TipoProficiencia.ARMAS,
                "Clavas, adagas, dardos, azagaias, maças, bordões, cimitarras, foices, fundas, lanças"));
        profs.add(criarProficiencia(c, TipoProficiencia.ARMADURAS,
                "Armaduras leves, armaduras médias, escudos (não metálicos)"));
        profs.add(criarProficiencia(c, TipoProficiencia.FERRAMENTAS,
                "Kit de herbalismo"));
        c.setListaProficienciasClasse(profs);
        return c;
    }

    private Classe feiticeiro() {
        Classe c = novaClasse(Classes.FEITICEIRO, DadosDeVida.D6, true, Atributos.CARISMA, TipoConjuracao.COMPLETO,
                List.of(Atributos.CONSTITUICAO, Atributos.CARISMA));
        List<Proficiencia> profs = new ArrayList<>();
        profs.add(criarProficiencia(c, TipoProficiencia.ARMAS,
                "Adagas, dardos, fundas, bordões, bestas leves"));
        c.setListaProficienciasClasse(profs);
        return c;
    }

    private Classe guerreiro() {
        Classe c = novaClasse(Classes.GUERREIRO, DadosDeVida.D10, false, null, TipoConjuracao.NENHUM,
                List.of(Atributos.FORCA, Atributos.CONSTITUICAO));
        List<Proficiencia> profs = new ArrayList<>();
        profs.add(criarProficiencia(c, TipoProficiencia.ARMAS,
                "Armas simples, armas marciais"));
        profs.add(criarProficiencia(c, TipoProficiencia.ARMADURAS,
                "Armaduras leves, armaduras médias, armaduras pesadas, escudos"));
        c.setListaProficienciasClasse(profs);
        return c;
    }

    private Classe ladino() {
        Classe c = novaClasse(Classes.LADINO, DadosDeVida.D8, false, null, TipoConjuracao.NENHUM,
                List.of(Atributos.DESTREZA, Atributos.INTELIGENCIA));
        List<Proficiencia> profs = new ArrayList<>();
        profs.add(criarProficiencia(c, TipoProficiencia.ARMAS,
                "Armas simples, bestas de mão, espadas longas, rapieiras, espadas curtas"));
        profs.add(criarProficiencia(c, TipoProficiencia.ARMADURAS, "Armaduras leves"));
        profs.add(criarProficiencia(c, TipoProficiencia.FERRAMENTAS,
                "Ferramentas de ladrão"));
        c.setListaProficienciasClasse(profs);
        return c;
    }

    private Classe mago() {
        Classe c = novaClasse(Classes.MAGO, DadosDeVida.D6, true, Atributos.INTELIGENCIA, TipoConjuracao.COMPLETO,
                List.of(Atributos.INTELIGENCIA, Atributos.SABEDORIA));
        List<Proficiencia> profs = new ArrayList<>();
        profs.add(criarProficiencia(c, TipoProficiencia.ARMAS,
                "Adagas, dardos, fundas, bordões, bestas leves"));
        c.setListaProficienciasClasse(profs);
        return c;
    }

    private Classe monge() {
        Classe c = novaClasse(Classes.MONGE, DadosDeVida.D8, false, null, TipoConjuracao.NENHUM,
                List.of(Atributos.FORCA, Atributos.DESTREZA));
        List<Proficiencia> profs = new ArrayList<>();
        profs.add(criarProficiencia(c, TipoProficiencia.ARMAS,
                "Armas simples, espadas curtas"));
        profs.add(criarProficiencia(c, TipoProficiencia.FERRAMENTAS,
                "Um tipo de ferramenta de artesão ou instrumento musical à escolha"));
        c.setListaProficienciasClasse(profs);
        return c;
    }

    private Classe paladino() {
        Classe c = novaClasse(Classes.PALADINO, DadosDeVida.D10, true, Atributos.CARISMA, TipoConjuracao.MEIO,
                List.of(Atributos.SABEDORIA, Atributos.CARISMA));
        List<Proficiencia> profs = new ArrayList<>();
        profs.add(criarProficiencia(c, TipoProficiencia.ARMAS,
                "Armas simples, armas marciais"));
        profs.add(criarProficiencia(c, TipoProficiencia.ARMADURAS,
                "Armaduras leves, armaduras médias, armaduras pesadas, escudos"));
        c.setListaProficienciasClasse(profs);
        return c;
    }

    private Classe patrulheiro() {
        Classe c = novaClasse(Classes.PATRULHEIRO, DadosDeVida.D10, true, Atributos.SABEDORIA, TipoConjuracao.MEIO,
                List.of(Atributos.FORCA, Atributos.DESTREZA));
        List<Proficiencia> profs = new ArrayList<>();
        profs.add(criarProficiencia(c, TipoProficiencia.ARMAS,
                "Armas simples, armas marciais"));
        profs.add(criarProficiencia(c, TipoProficiencia.ARMADURAS,
                "Armaduras leves, armaduras médias, escudos"));
        c.setListaProficienciasClasse(profs);
        return c;
    }
}