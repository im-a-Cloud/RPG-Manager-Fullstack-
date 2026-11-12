import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TesteConexao } from './teste-conexao';

describe('TesteConexao', () => {
  let component: TesteConexao;
  let fixture: ComponentFixture<TesteConexao>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TesteConexao],
    }).compileComponents();

    fixture = TestBed.createComponent(TesteConexao);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
