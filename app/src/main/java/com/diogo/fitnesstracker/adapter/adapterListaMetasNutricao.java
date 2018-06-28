package com.diogo.fitnesstracker.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.diogo.fitnesstracker.R;
import com.diogo.fitnesstracker.activities.MetasCalorias;
import com.diogo.fitnesstracker.activities.MetasRefeicao;
import com.diogo.fitnesstracker.model.ItemsRecycler;

import java.util.List;

public class adapterListaMetasNutricao extends RecyclerView.Adapter<adapterListaMetasNutricao.ViewHolder> {

    Context context;
    List<ItemsRecycler> dados;

    public adapterListaMetasNutricao(Context context, List<ItemsRecycler> dados) {
        this.context = context;
        this.dados = dados;
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lista_metas_nutricao,parent,false);
        final ViewHolder vHolder = new ViewHolder(v);

        vHolder.titulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int item = vHolder.getAdapterPosition();
                switch (item)
                {
                    case 0:
                        Intent i = new Intent(context,MetasCalorias.class);
                        context.startActivity(i);
                        break;
                    case 1:
                        Intent i2 = new Intent(context,MetasRefeicao.class);
                        context.startActivity(i2);
                        break;
                }
            }
        });
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.titulo.setText(dados.get(position).getTitulo());
        holder.descricao.setText(dados.get(position).getConteudo());
    }

    @Override
    public int getItemCount() {
        return dados.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView titulo,descricao;

        public ViewHolder(View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.textoListaTitulo);
            descricao = itemView.findViewById(R.id.textoListaDescricao);
        }
    }
}
