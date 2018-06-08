package com.diogo.fitnesstracker;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TerminaRegisto extends AppCompatActivity {

    private EditText editText_height,editText_weight;
    private TextView textView_height, textView_weight;
    private Button botao_done;
    private Animation fromSide;
    private Context context = TerminaRegisto.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termina_registo);
        editText_height = (EditText) findViewById(R.id.editText_height);
        editText_weight = (EditText) findViewById(R.id.editText_weight);

        editText_height.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO inserção do dialogo para a altura
                final Dialog dialogoAltura = new Dialog(context);
                dialogoAltura.setContentView(R.layout.costum_dialog);
                dialogoAltura.setTitle("Height");

                TextView textView_kg = (TextView) dialogoAltura.findViewById(R.id.textView_dialog);
                textView_kg.setText("Kg");
                EditText editText_dialogoAltura = (EditText) dialogoAltura.findViewById(R.id.editText_dialog);
                Button cancelar = (Button) dialogoAltura.findViewById(R.id.botao_cancelar);
                Button confirmar = (Button) dialogoAltura.findViewById(R.id.botao_confirmar);

                confirmar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogoAltura.dismiss();
                    }
                });
                dialogoAltura.show();
            }
        });

        editText_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO inserção do dialogo para o peso

            }
        });
    }


}
