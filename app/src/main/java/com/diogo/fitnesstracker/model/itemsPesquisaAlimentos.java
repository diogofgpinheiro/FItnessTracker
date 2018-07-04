package com.diogo.fitnesstracker.model;

public class itemsPesquisaAlimentos {

    private String nome;
    private String descricao;
    private String calorias;
    private String codigo;

    public itemsPesquisaAlimentos() {
    }

    public itemsPesquisaAlimentos(String nome, String descricao, String calorias,String codigo) {
        this.nome = nome;
        this.descricao = descricao;
        this.calorias = calorias;
        this.codigo = codigo;
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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
