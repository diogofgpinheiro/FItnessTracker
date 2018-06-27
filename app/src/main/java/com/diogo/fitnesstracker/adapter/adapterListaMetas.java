package com.diogo.fitnesstracker.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.diogo.fitnesstracker.helper.Calorias;
import com.diogo.fitnesstracker.helper.CodificadorBase64;
import com.diogo.fitnesstracker.model.ItemsRecycler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class adapterListaMetas  extends RecyclerView.Adapter<adapterListaMetas.ViewHolder> {

    Context context;
    List<ItemsRecycler> dados;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getReferenciaFirebase();
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getAutenticacao();
    private String[] itemsAtividade = {"Não muito ativo", "Levemente ativo", "Ativo", "Bastante ativo"};
    private String[] itemsMetaSemanal = {"Perder 1kg por semana", "Perder 0.5kg por semana", "Perder 0.25kg por semana", "Manter o peso","Ganhar 0.25kg por semana","Ganhar 0.5kg por semana"};


    public adapterListaMetas(Context context,List<ItemsRecycler> dados) {
        this.dados = dados;
        this.context = context;
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
                        Calorias calorias = new Calorias(63.2,177,"Feminino");
                        int cal = calorias.calculaCalorias("Ativo",0,"13/04/1996");
                        String texto = Integer.toString(cal);
                        break;
                    case 2:
                        criaDialogoMedidas("Meta do peso","kg","meta_peso");
                        //abrir dialogo para a meta de peso
                        break;
                    case 3:
                        //abrir dialogo para a meta semanal
                        criaDialgoAviso(3);
                        break;
                    case 4:
                        //abrir dialogo para a atividade
                        criaDialgoAviso(4);
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
                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        //TODO ler dados do utilizador e guardar
                        String IDUtilizador = CodificadorBase64.codificaBase64(autenticacao.getCurrentUser().getEmail());
                        Double peso = Double.parseDouble(editText_dialog.getText().toString());
                        firebaseRef.child("Metas").child(IDUtilizador).child("MetasPeso").child(valor).setValue(peso);
                    }
                })

                .setNegativeButton("Cancelar",
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

    public void criaDialogoAtividade() {
        android.app.AlertDialog.Builder mBuilder = new android.app.AlertDialog.Builder(context);
        mBuilder.setTitle("Nivel de Atividade");
        mBuilder.setSingleChoiceItems(itemsAtividade, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String IDUtilizador = CodificadorBase64.codificaBase64(autenticacao.getCurrentUser().getEmail());
                firebaseRef.child("Metas").child(IDUtilizador).child("MetasPeso").child("nivel_atividade").setValue(itemsAtividade[which]);
                dialog.dismiss();
            }
        });

        android.app.AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    public void criaDialogoMetaSemanal() {
        android.app.AlertDialog.Builder mBuilder = new android.app.AlertDialog.Builder(context);
        mBuilder.setTitle("Meta Semanal");
        mBuilder.setSingleChoiceItems(itemsMetaSemanal, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String IDUtilizador = CodificadorBase64.codificaBase64(autenticacao.getCurrentUser().getEmail());
                double valor = 0;
                switch (which)
                {
                    case 0:
                        valor = -1;
                        break;
                    case 1:
                        valor = -0.5;
                        break;
                    case 2:
                        valor = -0.25;
                        break;
                    case 3:
                        valor = 0;
                        break;
                    case 4:
                        valor = 0.25;
                        break;
                    case 5:
                        valor = 0.5;
                        break;
                }
                firebaseRef.child("Metas").child(IDUtilizador).child("MetasPeso").child("meta_semanal").setValue(valor);
                dialog.dismiss();
            }
        });

        android.app.AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }


    public void criaDialgoAviso(final int opcao)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Tem a certeza?");
        builder.setMessage("Ao atualizar este valor será necessário recalcular as metas calóricas e de nutrientes substituindo as configurações atuais. Deseja continuar?");
        builder.setPositiveButton("Confimar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (opcao)
                {
                    case 3:
                        criaDialogoMetaSemanal();
                        break;
                    case 4:
                        criaDialogoAtividade();
                        break;
                }

            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog dialog = builder.create();
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
