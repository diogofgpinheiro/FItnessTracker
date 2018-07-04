package com.diogo.fitnesstracker.adapter;

import android.content.Context;
import android.content.Intent;
import android.icu.text.UnicodeSetSpanner;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.diogo.fitnesstracker.R;
import com.diogo.fitnesstracker.activities.Alimento;
import com.diogo.fitnesstracker.activities.PesquisaAlimentos;
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

        vHolder.cardViewPesquisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = vHolder.getAdapterPosition();
                String codigo = dados.get(position).getCodigo();
                Intent i = new Intent(context,Alimento.class);
                i.putExtra("IDAlimento",codigo);
                context.startActivity(i);
            }
        });

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
        CardView cardViewPesquisa;
        public ViewHolder(View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.textoNomeAlimento);
            descricao = itemView.findViewById(R.id.textoDescricaoAlimento);
            cal = itemView.findViewById(R.id.textoCaloriasAlimento);
            cardViewPesquisa = itemView.findViewById(R.id.cardViewPesquisa);
        }
    }
}
