package com.diogo.fitnesstracker.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.diogo.fitnesstracker.R;
import com.diogo.fitnesstracker.model.Utilizador;

public class Registar2 extends AppCompatActivity {

    private EditText campoAltura, campoPeso, campoAtividade;
    private Context context = Registar2.this;
    private Button botaoProximo;
    private String[] listItems = {"Não muito ativo", "Levemente ativo", "Ativo", "Bastante ativo"};
    private Boolean verificaAltura = false, verificaPeso = false, verificaAtividade = false;
    private Bundle extras;
    private String textoNome, textoData, textoGenero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registar2);
        extras = getIntent().getExtras();
        campoAltura = findViewById(R.id.editAltura);
        campoPeso = findViewById(R.id.editPeso);
        campoAtividade = findViewById(R.id.editAtividade);
        botaoProximo = findViewById(R.id.botaoProximo);

        campoAltura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //inserção do dialogo para a altura
                criaDialogo("Altura", "cm", campoAltura);
            }
        });

        campoPeso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //inserção do dialogo para o peso
                criaDialogo("Peso", "kg", campoPeso);
            }
        });

        campoAtividade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criaDialogoAtividade();
            }
        });

        botaoProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textoAltura = campoAltura.getText().toString();
                String textoPeso = campoPeso.getText().toString();
                String textoAtividade = campoAtividade.getText().toString();

                if (textoAltura.isEmpty()) {
                    campoAltura.setError("Por favor preencha este campo");
                    return;
                }
                if (textoPeso.isEmpty()) {
                    campoPeso.setError("Por favor preencha este campo");
                    return;
                }

                if (textoAtividade.isEmpty()) {
                    campoAtividade.setError("Por favor preencha este campo");
                    return;
                }

                if (extras != null) {
                    textoNome = extras.getString("NOME");
                    textoData = extras.getString("DATA");
                    textoGenero = extras.getString("GENERO");
                }
                Intent intent = new Intent(Registar2.this, Registar3.class);
                intent.putExtra("NOME", textoNome);
                intent.putExtra("DATA", textoData);
                intent.putExtra("GENERO", textoGenero);
                intent.putExtra("ALTURA", textoAltura);
                intent.putExtra("PESO", textoPeso);
                intent.putExtra("ATIVIDADE", textoAtividade);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });
    }

    public void criaDialogo(String titulo, String medida, final EditText editText) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);//obtem um layout xml para uma view
        View mView = layoutInflater.inflate(R.layout.costum_dialog, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(mView);

        final EditText editText_dialog =  mView.findViewById(R.id.editText_dialog);
        TextView textView_titulo = mView.findViewById(R.id.textview_titulo);
        TextView textView_medida = mView.findViewById(R.id.textview_medida);

        textView_titulo.setText(titulo);
        textView_medida.setText(medida);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        //TODO ler dados do utilizador e guardar
                        editText.setText(editText_dialog.getText());
                        editText.setError(null);
                    }
                })

                .setNegativeButton("Cancelar",
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

    public void criaDialogoAtividade() {
        android.app.AlertDialog.Builder mBuilder = new android.app.AlertDialog.Builder(this);
        mBuilder.setTitle("Nivel de Atividade");
        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                campoAtividade.setText(listItems[which]);
                campoAtividade.setError(null);
                dialog.dismiss();
            }
        });

        android.app.AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }
}
