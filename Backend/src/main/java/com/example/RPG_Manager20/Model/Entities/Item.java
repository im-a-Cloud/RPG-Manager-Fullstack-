package com.example.RPG_Manager20.Model.Entities;

import com.example.RPG_Manager20.Model.Enums.RaridadeItem;
import com.example.RPG_Manager20.Model.Enums.TipoItem;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name= "tb_item")
public class Item extends AbstractModel{
    private String nomeItem;
    private TipoItem tipoItem;
    private String descricaoItem;
    private double precoItem;
    private RaridadeItem raridadeItem;
    private double pesoItem;
    private boolean isMagico;
    private boolean precisaSintonizacao;
    private int quantidade;

    public Item(){

    }
    public Item(String nomeItem, TipoItem tipoItem, String descricaoItem, double precoItem, RaridadeItem raridadeItem, double pesoItem, boolean isMagico, boolean precisaSintonizacao, int quantidade) {
        this.nomeItem = nomeItem;
        this.tipoItem = tipoItem;
        this.descricaoItem = descricaoItem;
        this.precoItem = precoItem;
        this.raridadeItem = raridadeItem;
        this.pesoItem = pesoItem;
        this.isMagico = isMagico;
        this.precisaSintonizacao = precisaSintonizacao;
        this.quantidade = quantidade;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public TipoItem getTipoItem() {
        return tipoItem;
    }

    public void setTipoItem(TipoItem tipoItem) {
        this.tipoItem = tipoItem;
    }

    public boolean isMagico() {
        return isMagico;
    }

    public void setMagico(boolean magico) {
        isMagico = magico;
    }

    public boolean isPrecisaSintonizacao() {
        return precisaSintonizacao;
    }

    public void setPrecisaSintonizacao(boolean precisaSintonizacao) {
        this.precisaSintonizacao = precisaSintonizacao;
    }

    public double getPesoItem() {
        return pesoItem;
    }

    public void setPesoItem(double pesoItem) {
        this.pesoItem = pesoItem;
    }

    public String getNomeItem() {
        return nomeItem;
    }

    public void setNomeItem(String nomeItem) {
        this.nomeItem = nomeItem;
    }

    public String getDescricaoItem() {
        return descricaoItem;
    }

    public void setDescricaoItem(String descricaoItem) {
        this.descricaoItem = descricaoItem;
    }

    public double getPrecoItem() {
        return precoItem;
    }

    public void setPrecoItem(double precoItem) {
        this.precoItem = precoItem;
    }

    public RaridadeItem getRaridadeItem() {
        return raridadeItem;
    }

    public void setRaridadeItem(RaridadeItem raridadeItem) {
        this.raridadeItem = raridadeItem;
    }
}
