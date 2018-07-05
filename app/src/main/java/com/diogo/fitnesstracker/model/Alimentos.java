package com.diogo.fitnesstracker.model;

import com.diogo.fitnesstracker.config.ConfiguracaoFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

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

    public void gravar(String data,String id,String refeicao)
    {
        DatabaseReference referenciaFirebase = ConfiguracaoFirebase.getReferenciaFirebase();
        String key = referenciaFirebase.child("Alimentos")
                .push().getKey();
        referenciaFirebase.child("Alimentos")
                .child(key)
                .setValue(this);
        referenciaFirebase.child("Refeicoes").child(id).child(data).child(refeicao).child(key).setValue(this);
        referenciaFirebase.child("Recentes").child(id).child(key).setValue(this);

    }


    public void adicionaRefeicao(String id,String codigo,String refeicao,String data)
    {
        DatabaseReference referenciaFirebase = ConfiguracaoFirebase.getReferenciaFirebase();
        referenciaFirebase.child("Refeicoes").child(id).child(data).child(refeicao).child(codigo).setValue(this);
        referenciaFirebase.child("Recentes").child(id).child(codigo).setValue(this);
    }
}
