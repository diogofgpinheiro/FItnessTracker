package com.diogo.fitnesstracker.helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;

public class Calorias {

    private double peso;
    private double altura;
    private String genero;
    private int calorias;
    private int idade;
    private double nivel_atividade;


    public Calorias() {
    }

    public Calorias(double peso, double altura, String genero) {
        this.peso = peso;
        this.altura = altura;
        this.genero = genero;
    }

    public int calculaCalorias(String nivelAtividade,double meta_semanal,String data)
    {

        idade = obtemIdade(data);

        if(nivelAtividade.equals("Não muito ativo"))
        {
            nivel_atividade = 1.2;
        }else if(nivelAtividade.equals("Levemente ativo"))
        {
            nivel_atividade = 1.375;
        }else if (nivelAtividade.equals("Ativo"))
        {
            nivel_atividade = 1.55;
        }else
        {
            nivel_atividade = 1.725;
        }

        meta_semanal = meta_semanal * 1000;

        if(genero.equals("Masculino"))
        {
            calorias = (int) ((((10*peso) + (6.25*altura) - (5*idade) + 5) * nivel_atividade) + meta_semanal);

        }else
        {
            calorias = (int) ((((10*peso) + (6.25*altura) - (5*idade) - 161)* nivel_atividade) + meta_semanal);
        }

        return calorias;
    }

    private int obtemIdade(String data)
    {
        SimpleDateFormat format = new SimpleDateFormat("dd/mm/yyyy");
        Date date;
        int idade;

        try {
            date = format.parse(data);
            Date currentTime = Calendar.getInstance().getTime();
            idade = getDiffYears(date,currentTime);

            return idade;
        }catch (Exception e)
        {
            e.printStackTrace();
            return 0;
        }
    }

    public static int getDiffYears(Date first, Date last) {
        Calendar a = getCalendar(first);
        Calendar b = getCalendar(last);
        int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
        if (a.get(MONTH) > b.get(MONTH) ||
                (a.get(MONTH) == b.get(MONTH) && a.get(DATE) > b.get(DATE))) {
            diff--;
        }
        return diff;
    }

    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTime(date);
        return cal;
    }
}
