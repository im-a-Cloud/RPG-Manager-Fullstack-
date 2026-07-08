import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CriarFichaPersonagemComponent } from './criar-ficha-personagem.component';

describe('CriarFichaPersonagem', () => {
  let component: CriarFichaPersonagemComponent;
  let fixture: ComponentFixture<CriarFichaPersonagemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CriarFichaPersonagemComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(CriarFichaPersonagemComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
