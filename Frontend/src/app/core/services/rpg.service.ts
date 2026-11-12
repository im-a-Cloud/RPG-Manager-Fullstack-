import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class RpgService {

  calcularBonusAtributo(valor: number): number {
    return Math.floor((valor - 10) / 2);
  }

  calcularBonusProficiencia(nivel: number): number {
    return Math.floor((nivel - 1) / 4) + 2;
  }

  getAtributoPorPericia(
    pericia: string,
    periciasPorAtributo: { [key: string]: string[] }
  ): string {

    for (const [atributo, pericias] of Object.entries(periciasPorAtributo)) {
      if (pericias.includes(pericia)) {
        return atributo;
      }
    }

    return '';
  }

  getBonusAtributo(
    atributo: string,
    atributos: {
      forca: number;
      destreza: number;
      constituicao: number;
      inteligencia: number;
      sabedoria: number;
      carisma: number;
    }
  ): number {

    switch (atributo) {

      case 'forca':
        return this.calcularBonusAtributo(atributos.forca);

      case 'destreza':
        return this.calcularBonusAtributo(atributos.destreza);

      case 'constituicao':
        return this.calcularBonusAtributo(atributos.constituicao);

      case 'inteligencia':
        return this.calcularBonusAtributo(atributos.inteligencia);

      case 'sabedoria':
        return this.calcularBonusAtributo(atributos.sabedoria);

      case 'carisma':
        return this.calcularBonusAtributo(atributos.carisma);

      default:
        return 0;
    }
  }
  getValorPericia(
    nome: string,
    pericias: { [key: string]: boolean },
    nivel: number,
    atributos: {
      forca: number;
      destreza: number;
      constituicao: number;
      inteligencia: number;
      sabedoria: number;
      carisma: number;
    },
    periciasPorAtributo: { [key: string]: string[] }
  ): number {

    const atributo = this.getAtributoPorPericia(
      nome,
      periciasPorAtributo
    );

    const bonus = this.getBonusAtributo(
      atributo,
      atributos
    );

    if (pericias[nome]) {
      return bonus + this.calcularBonusProficiencia(nivel);
    }

    return bonus;
  }

  getTesteResistencia(
    atributo: string,
    resistencias: { [key: string]: boolean },
    nivel: number,
    atributos: {
      forca: number;
      destreza: number;
      constituicao: number;
      inteligencia: number;
      sabedoria: number;
      carisma: number;
    }
  ): number {

    const bonus = this.getBonusAtributo(
      atributo,
      atributos
    );

    if (resistencias[atributo]) {
      return bonus + this.calcularBonusProficiencia(nivel);
    }

    return bonus;
  }
  
}