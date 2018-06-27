package com.diogo.fitnesstracker.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.diogo.fitnesstracker.R;
import com.diogo.fitnesstracker.model.ItemsRecycler;

import java.util.List;

public class adapterListaMetas  extends RecyclerView.Adapter<adapterListaMetas.ViewHolder> {

    Context context;
    List<ItemsRecycler> dados;


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

}
