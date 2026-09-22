package com.example.RPG_Manager20.Config;

import com.example.RPG_Manager20.Model.Entities.Pericia;
import com.example.RPG_Manager20.Model.Enums.Atributos;
import com.example.RPG_Manager20.Repository.PericiaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PericiaDataLoader implements CommandLineRunner {

    private final PericiaRepository periciaRepository;

    public PericiaDataLoader(PericiaRepository periciaRepository) {
        this.periciaRepository = periciaRepository;
    }

    @Override
    public void run(String... args) {
        if (periciaRepository.count() > 0) {
            return;
        }

        periciaRepository.saveAll(List.of(
                build("acrobacia",        "Acrobacia",        Atributos.DESTREZA),
                build("adestrar_animais", "Adestrar Animais", Atributos.SABEDORIA),
                build("arcanismo",        "Arcanismo",        Atributos.INTELIGENCIA),
                build("atletismo",        "Atletismo",        Atributos.FORCA),
                build("atuacao",          "Atuação",          Atributos.CARISMA),
                build("enganacao",        "Enganação",        Atributos.CARISMA),
                build("furtividade",      "Furtividade",      Atributos.DESTREZA),
                build("historia",         "História",         Atributos.INTELIGENCIA),
                build("intimidacao",      "Intimidação",      Atributos.CARISMA),
                build("intuicao",         "Intuição",         Atributos.SABEDORIA),
                build("investigacao",     "Investigação",     Atributos.INTELIGENCIA),
                build("medicina",         "Medicina",         Atributos.SABEDORIA),
                build("natureza",         "Natureza",         Atributos.INTELIGENCIA),
                build("percepcao",        "Percepção",        Atributos.SABEDORIA),
                build("persuasao",        "Persuasão",        Atributos.CARISMA),
                build("prestidigitacao",  "Prestidigitação",  Atributos.DESTREZA),
                build("religiao",         "Religião",         Atributos.INTELIGENCIA),
                build("sobrevivencia",    "Sobrevivência",    Atributos.SABEDORIA)
        ));

        System.out.println("✅ Catálogo de perícias populado: " + periciaRepository.count() + " registros");
    }

    private Pericia build(String slug, String nomeExibicao, Atributos atributo) {
        Pericia p = new Pericia();
        p.setSlug(slug);
        p.setNomeExibicao(nomeExibicao);
        p.setAtributoChave(atributo);
        p.setValorTotal(0);
        return p;
    }
}