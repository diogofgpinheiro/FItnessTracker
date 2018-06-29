package com.diogo.fitnesstracker.model;

public class itemsPesquisaAlimentos {

    private String nome;
    private String descricao;
    private String calorias;

    public itemsPesquisaAlimentos() {
    }

    public itemsPesquisaAlimentos(String nome, String descricao, String calorias) {
        this.nome = nome;
        this.descricao = descricao;
        this.calorias = calorias;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCalorias() {
        return calorias;
    }

    public void setCalorias(String calorias) {
        this.calorias = calorias;
    }
}
