package com.diogo.fitnesstracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;

public class PaginaResgito2 extends AppCompatActivity {

    private EditText data;
    String[] listItems = {"Male", "Female"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_resgito2);
        data = (EditText) findViewById(R.id.Data);

        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostraDialogoGenero(v);
            }
        });
    }

    private void mostraDialogoGenero(View v) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle("Gender");
        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                data.setText(listItems[which]);
                dialog.dismiss();
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

}
