package com.diogo.fitnesstracker;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

public class PaginaResgito2 extends AppCompatActivity {

    private EditText data_editText,genero_editText;
    private TextView data_textView,genero_textView;
    private Button botao_next;
    private Animation fromSide,voltaAtras;
    private String[] listItems = {"Male", "Female"};
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_resgito2);
        insereAnimacoesInicio();
        data_editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostraDialogoData();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String data = dayOfMonth + "/" + month + "/" + year;
                data_editText.setText(data);
            }
        };

        genero_editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostraDialogoGenero(v);
            }
        });
    }

    private void insereAnimacoesInicio() {
        botao_next = (Button) findViewById(R.id.botao_proximo2);
        data_editText = (EditText) findViewById(R.id.editText_data);
        genero_editText = (EditText) findViewById(R.id.editText_genero);
        data_textView = (TextView) findViewById(R.id.textview_data);
        genero_textView = (TextView) findViewById(R.id.textview_genero);
        fromSide = AnimationUtils.loadAnimation(this, R.anim.from_side);
        genero_editText.setAnimation(fromSide);
        data_editText.setAnimation(fromSide);
        genero_textView.setAnimation(fromSide);
        data_textView.setAnimation(fromSide);
        botao_next.setAnimation(fromSide);
    }

    private void insereAnimacoesVoltar()
    {
        voltaAtras = AnimationUtils.loadAnimation(this, R.anim.slide_in_dir);
        voltaAtras.reset();
        genero_editText.setAnimation(voltaAtras);
        data_editText.setAnimation(voltaAtras);
        genero_textView.setAnimation(voltaAtras);
        data_textView.setAnimation(voltaAtras);
        botao_next.setAnimation(voltaAtras);
        voltaAtras.startNow();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        insereAnimacoesVoltar();
        overridePendingTransition(R.anim.slide_in_dir,R.anim.slide_out_dir);
    }

    private void mostraDialogoGenero(View v) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle("Gender");
        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                genero_editText.setText(listItems[which]);
                dialog.dismiss();
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    private void mostraDialogoData()
    {
        Calendar calendario = Calendar.getInstance();
        int ano = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialogData = new DatePickerDialog(PaginaResgito2.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                ano,mes,dia);
        dialogData.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogData.show();
    }

}
