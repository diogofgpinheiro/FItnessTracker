package com.diogo.fitnesstracker.adapter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.diogo.fitnesstracker.R;
import com.diogo.fitnesstracker.config.ConfiguracaoFirebase;
import com.diogo.fitnesstracker.helper.CodificadorBase64;
import com.diogo.fitnesstracker.model.Perfil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.Calendar;
import java.util.List;


public class adapterListaPerfil extends RecyclerView.Adapter<adapterListaPerfil.ViewHolder> {

    Context context;
    List<Perfil> dados;
    private Double Altura;
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getAutenticacao();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getReferenciaFirebase();
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private String[] itemsDialogo = {"Masculino", "Feminino"};


    public adapterListaPerfil(Context context,List<Perfil> dados) {
        this.context = context;
        this.dados = dados;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lista_perfil,parent,false);
        final ViewHolder vHolder = new ViewHolder(v);

        vHolder.conteudo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int item = vHolder.getAdapterPosition();
                switch (item)
                {
                    case 0:
                        criaDialogoNome();
                        break;
                    case 1:
                        criaDialogoMedidas("Altura","cm","altura");
                        break;
                    case 2:
                        criaDialogoMedidas("Peso","kg","peso");
                        break;
                    case 3:
                        criaDialogoGenero();
                        break;
                    case 4:
                        criaDialogoData();
                        break;
                    case 5:
                        break;
                }
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String data = dayOfMonth + "/" + month + "/" + year;
                String IDUtilizador = CodificadorBase64.codificaBase64(autenticacao.getCurrentUser().getEmail());
                firebaseRef.child("Utilizadores").child(IDUtilizador).child("data_nascimento").setValue(data);
            }
        };

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.titulo.setText(dados.get(position).getTitulo());
        holder.conteudo.setText(dados.get(position).getConteudo());

    }

    @Override
    public int getItemCount() {
        return dados.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView titulo,conteudo;

        public ViewHolder(View itemView)
        {
            super(itemView);
            titulo = itemView.findViewById(R.id.textoListaTitulo);
            conteudo = itemView.findViewById(R.id.textoListaConteudo);
        }
    }

    public void criaDialogoMedidas(String titulo, String medida,final String valor) {
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
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        //TODO ler dados do utilizador e guardar
                        String IDUtilizador = CodificadorBase64.codificaBase64(autenticacao.getCurrentUser().getEmail());
                        Double altura = Double.parseDouble(editText_dialog.getText().toString());
                        firebaseRef.child("Utilizadores").child(IDUtilizador).child(valor).setValue(altura);
                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();                            }
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

    public void criaDialogoData()
    {
        Calendar calendario = Calendar.getInstance();
        int ano = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialogData = new DatePickerDialog(context,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                ano,mes,dia);
        dialogData.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogData.show();
    }

    public void criaDialogoGenero() {
        android.app.AlertDialog.Builder mBuilder = new android.app.AlertDialog.Builder(context);
        mBuilder.setTitle("Género");
        mBuilder.setSingleChoiceItems(itemsDialogo, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String IDUtilizador = CodificadorBase64.codificaBase64(autenticacao.getCurrentUser().getEmail());
                firebaseRef.child("Utilizadores").child(IDUtilizador).child("sexo").setValue(itemsDialogo[which]);
                dialog.dismiss();
            }
        });
        android.app.AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }


    public void criaDialogoNome()
    {
        LayoutInflater layoutInflater = LayoutInflater.from(context);//obtem um layout xml para uma view
        View mView = layoutInflater.inflate(R.layout.dialogo_nome_perfil, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(mView);

        final EditText editText =  mView.findViewById(R.id.editTextDialogo);

        alertDialogBuilder
                .setCancelable(false)
            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogBox, int id) {
                    //TODO ler dados do utilizador e guardar
                    String IDUtilizador = CodificadorBase64.codificaBase64(autenticacao.getCurrentUser().getEmail());
                    String nome = editText.getText().toString();
                    firebaseRef.child("Utilizadores").child(IDUtilizador).child("nome").setValue(nome);
                }
            })

            .setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogBox, int id) {
                            dialogBox.cancel();                            }
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


