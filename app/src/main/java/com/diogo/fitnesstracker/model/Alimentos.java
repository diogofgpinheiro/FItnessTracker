package com.diogo.fitnesstracker.model;

public class Alimentos {

    private String nome;
    private int calorias;
    private String marca;
    private Long codigo_barras;
    private double carboidratos;
    private double gorduras;
    private double proteinas;
    private double quantidade;
    private String unidade;

    public Alimentos() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCalorias() {
        return calorias;
    }

    public void setCalorias(int calorias) {
        this.calorias = calorias;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public double getCarboidratos() {
        return carboidratos;
    }
    public void setCarboidratos(double carboidratos) {
        this.carboidratos = carboidratos;
    }

    public double getGorduras() {
        return gorduras;
    }

    public void setGorduras(double gorduras) {
        this.gorduras = gorduras;
    }

    public double getProteinas() {
        return proteinas;
    }

    public void setProteinas(double proteinas) {
        this.proteinas = proteinas;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public Long getCodigo_barras() {
        return codigo_barras;
    }

    public void setCodigo_barras(Long codigo_barras) {
        this.codigo_barras = codigo_barras;
    }
}
