package com.diogo.fitnesstracker.model;

import com.diogo.fitnesstracker.config.ConfiguracaoFirebase;
import com.diogo.fitnesstracker.helper.CodificadorBase64;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class MetasPeso {

    private double peso_inicial;
    private double peso;
    private double meta_peso;
    private double meta_semanal;
    private String nivel_atividade;
    private String data_inicial;

    public double getPeso_inicial() {
        return peso_inicial;
    }

    public void setPeso_inicial(double peso_inicial) {
        this.peso_inicial = peso_inicial;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public double getMeta_peso() {
        return meta_peso;
    }

    public void setMeta_peso(double meta_peso) {
        this.meta_peso = meta_peso;
    }

    public double getMeta_semanal() {
        return meta_semanal;
    }

    public void setMeta_semanal(double meta_semanal) {
        this.meta_semanal = meta_semanal;
    }

    public String getNivel_atividade() {
        return nivel_atividade;
    }

    public void setNivel_atividade(String nivel_atividade) {
        this.nivel_atividade = nivel_atividade;
    }

    public String getData_inicial() {
        return data_inicial;
    }

    public void setData_inicial(String data_incial) {
        this.data_inicial = data_incial;
    }

    public void gravar(String IDUtilizador)
    {
        DatabaseReference referenciaFirebase = ConfiguracaoFirebase.getReferenciaFirebase();
        referenciaFirebase.child("Metas")
                .child(IDUtilizador)
                .child("MetasPeso")
                .setValue(this);
    }
}
