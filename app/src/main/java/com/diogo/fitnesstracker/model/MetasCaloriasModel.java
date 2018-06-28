package com.diogo.fitnesstracker.model;

public class MetasCaloriasModel {

    private int Calorias;
    private double percentagem_carbo;
    private double percentagem_gordura;
    private double percentagem_proteina;
    private double perc_peqAlmoco;
    private double perc_almoco;
    private double perc_lanche;
    private double perc_jantar;



    public MetasCaloriasModel() {

    }

    public int getCalorias() {
        return Calorias;
    }

    public void setCalorias(int calorias) {
        Calorias = calorias;
    }

    public double getPercentagem_carbo() {
        return percentagem_carbo;
    }

    public void setPercentagem_carbo(double percentagem_carbo) {
        this.percentagem_carbo = percentagem_carbo;
    }

    public double getPercentagem_gordura() {
        return percentagem_gordura;
    }

    public void setPercentagem_gordura(double percentagem_gordura) {
        this.percentagem_gordura = percentagem_gordura;
    }

    public double getPercentagem_proteina() {
        return percentagem_proteina;
    }

    public void setPercentagem_proteina(double percentagem_proteina) {
        this.percentagem_proteina = percentagem_proteina;
    }

    public double getPerc_peqAlmoco() {
        return perc_peqAlmoco;
    }

    public void setPerc_peqAlmoco(double perc_peqAlmoco) {
        this.perc_peqAlmoco = perc_peqAlmoco;
    }

    public double getPerc_almoco() {
        return perc_almoco;
    }

    public void setPerc_almoco(double perc_almoco) {
        this.perc_almoco = perc_almoco;
    }

    public double getPerc_lanche() {
        return perc_lanche;
    }

    public void setPerc_lanche(double perc_lanche) {
        this.perc_lanche = perc_lanche;
    }

    public double getPerc_jantar() {
        return perc_jantar;
    }

    public void setPerc_jantar(double perc_jantar) {
        this.perc_jantar = perc_jantar;
    }
}
