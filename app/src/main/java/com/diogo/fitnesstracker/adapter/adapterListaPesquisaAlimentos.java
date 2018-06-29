package com.diogo.fitnesstracker.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diogo.fitnesstracker.R;
import com.diogo.fitnesstracker.model.itemsPesquisaAlimentos;

import java.util.List;

public class adapterListaPesquisaAlimentos extends RecyclerView.Adapter<adapterListaPesquisaAlimentos.ViewHolder>{

    Context context;
    List<itemsPesquisaAlimentos> dados;

    public adapterListaPesquisaAlimentos(Context context, List<itemsPesquisaAlimentos> dados) {
        this.context = context;
        this.dados = dados;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lista_pesquisa,parent,false);
        final ViewHolder vHolder = new ViewHolder(v);

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.nome.setText(dados.get(position).getNome());
        holder.descricao.setText(dados.get(position).getDescricao());
        holder.cal.setText(dados.get(position).getCalorias());
    }

    @Override
    public int getItemCount() {
        return dados.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView nome,descricao,cal;
        public ViewHolder(View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.textoNomeAlimento);
            descricao = itemView.findViewById(R.id.textoDescricaoAlimento);
            cal = itemView.findViewById(R.id.textoCaloriasAlimento);
        }
    }
}
