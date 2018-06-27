package com.diogo.fitnesstracker.model;

public class ItemsRecycler {

    private String titulo;
    private String conteudo;

    public ItemsRecycler() {

    }

    public ItemsRecycler(String titulo, String conteudo) {
        this.titulo = titulo;
        this.conteudo = conteudo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }
}
