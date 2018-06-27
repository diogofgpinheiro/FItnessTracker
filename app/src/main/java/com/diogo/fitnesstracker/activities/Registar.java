package com.diogo.fitnesstracker.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.diogo.fitnesstracker.R;
import com.diogo.fitnesstracker.model.Utilizador;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

public class Registar extends AppCompatActivity {

    private EditText campoData, campoGenero, campoNome;
    private Boolean verificaData = false, verificaGenero = false, verificaNome = false;
    private Button botao_next;
    private String[] listItems = {"Masculino", "Feminino"};
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registar);

        campoNome = findViewById(R.id.editNome);
        campoData = findViewById(R.id.editData);
        campoGenero = findViewById(R.id.editGenero);
        botao_next = findViewById(R.id.botaoProximo);

        campoData.setOnClickListener(new View.OnClickListener() {
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
                campoData.setText(data);
            }
        };

        campoGenero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostraDialogoGenero(v);
            }
        });

        botao_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textoNome = campoNome.getText().toString();
                String textoData = campoData.getText().toString();
                String textGenero = campoGenero.getText().toString();

                if (textoNome.isEmpty()) {
                    campoNome.setError("Por favor preencha este campo");
                    return;
                }


                if (textGenero.isEmpty()) {
                    campoGenero.setError("Por favor preencha este campo");
                    return;
                }

                if (textoData.isEmpty()) {
                    campoData.setError("Por favor preencha este campo");
                    return;
                }

                Intent intent = new Intent(Registar.this, Registar2.class);
                intent.putExtra("NOME", textoNome);
                intent.putExtra("GENERO", textGenero);
                intent.putExtra("DATA", textoData);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //adiciona animação ao voltar atrás
        overridePendingTransition(R.anim.slide_in_dir, R.anim.slide_out_dir);
    }

    private void mostraDialogoGenero(View v) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle("Género");
        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                campoGenero.setText(listItems[which]);
                campoGenero.setError(null);
                dialog.dismiss();
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    private void mostraDialogoData() {
        Calendar calendario = Calendar.getInstance();
        int ano = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialogData = new DatePickerDialog(Registar.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                ano, mes, dia);
        dialogData.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        campoData.setError(null);
        dialogData.show();
    }

}
