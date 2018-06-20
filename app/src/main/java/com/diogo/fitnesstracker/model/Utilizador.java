package com.diogo.fitnesstracker.model;

import com.diogo.fitnesstracker.config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

public class Utilizador {

    private String IdUtilizador;
    private String nome;
    private String email;
    private String password;
    private String data_nascimento;
    private double peso;
    private double altura;
    private String sexo;
    private String data_criacao;
    private String atividade;

    public String getAtividade() {
        return atividade;
    }

    public void setAtividade(String atividade) {
        this.atividade = atividade;
    }

    @Exclude
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Exclude
    public String getIdUtilizador() {
        return IdUtilizador;
    }

    public void setIdUtilizador(String idUtilizador) {
        IdUtilizador = idUtilizador;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getData_nascimento() {
        return data_nascimento;
    }

    public void setData_nascimento(String data_nascimento) {
        this.data_nascimento = data_nascimento;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public double getAltura() {
        return altura;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getData_criacao() {
        return data_criacao;
    }

    public void setData_criacao(String data_criacao) {
        this.data_criacao = data_criacao;
    }

    public void gravar()
    {
        DatabaseReference referenciaFirebase = ConfiguracaoFirebase.getReferenciaFirebase();
        referenciaFirebase.child("Utilizadores")
                .child(this.IdUtilizador)
                .setValue(this);
    }
}
