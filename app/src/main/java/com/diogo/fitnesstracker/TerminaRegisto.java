package com.diogo.fitnesstracker;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
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
        botao_done.setEnabled(false);
        editText_height = (EditText) findViewById(R.id.editText_height);
        editText_weight = (EditText) findViewById(R.id.editText_weight);

        editText_height.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO inserção do dialogo para a altura
                criaDialogo("Height","cm",editText_height);
            }
        });

        editText_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO inserção do dialogo para o peso
                criaDialogo("Weight","kg",editText_weight);
            }
        });
    }

    private void criaDialogo(String titulo, String medida, final EditText editText)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(context);//obtem um layout xml para uma view
        View mView = layoutInflater.inflate(R.layout.costum_dialog, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(mView);

        final EditText editText_dialog = (EditText) mView.findViewById(R.id.editText_dialog);
        TextView textView_titulo = (TextView)  mView.findViewById(R.id.textview_titulo);
        TextView textView_medida = (TextView)  mView.findViewById(R.id.textview_medida);

        textView_titulo.setText(titulo);
        textView_medida.setText(medida);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        //TODO ler dados do utilizador e guardar
                        editText.setText(editText_dialog.getText());
                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog dialog = alertDialogBuilder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#39796b"));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#39796b"));
            }
        });
        dialog.show();
    }
}
