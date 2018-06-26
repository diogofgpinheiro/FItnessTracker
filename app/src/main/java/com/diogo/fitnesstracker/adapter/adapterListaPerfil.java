package com.diogo.fitnesstracker.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.diogo.fitnesstracker.R;
import com.diogo.fitnesstracker.model.Perfil;

import java.util.List;


public class adapterListaPerfil extends RecyclerView.Adapter<adapterListaPerfil.ViewHolder> {

    Context context;
    List<Perfil> dados;


    public adapterListaPerfil(Context context,List<Perfil> dados) {
        this.context = context;
        this.dados = dados;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lista_perfil,parent,false);
        return new ViewHolder(v);
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
}


