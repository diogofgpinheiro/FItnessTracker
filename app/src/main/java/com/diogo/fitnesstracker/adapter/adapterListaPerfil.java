package com.diogo.fitnesstracker.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.diogo.fitnesstracker.R;
import com.diogo.fitnesstracker.config.ConfiguracaoFirebase;
import com.diogo.fitnesstracker.helper.CodificadorBase64;
import com.diogo.fitnesstracker.model.Perfil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.List;


public class adapterListaPerfil extends RecyclerView.Adapter<adapterListaPerfil.ViewHolder> {

    Context context;
    List<Perfil> dados;
    private Double Altura;
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getAutenticacao();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getReferenciaFirebase();

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

                        break;
                    case 1:
                        criaDialogo("Altura","cm");
                        break;
                    case 2:
                        criaDialogo("Peso","kg");
                        break;
                }
            }
        });

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

    public void criaDialogo(String titulo, String medida) {
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
                        firebaseRef.child("Utilizadores").child(IDUtilizador).child("altura").setValue(altura);
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


