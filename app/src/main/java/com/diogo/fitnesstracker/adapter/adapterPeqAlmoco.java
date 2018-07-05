package com.diogo.fitnesstracker.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.diogo.fitnesstracker.Fragments.DiarioFragment;
import com.diogo.fitnesstracker.R;
import com.diogo.fitnesstracker.activities.Alimento;
import com.diogo.fitnesstracker.config.ConfiguracaoFirebase;
import com.diogo.fitnesstracker.helper.CodificadorBase64;
import com.diogo.fitnesstracker.model.itemsPesquisaAlimentos;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class adapterPeqAlmoco extends RecyclerView.Adapter<adapterPeqAlmoco.ViewHolder> {


    Context context;
    List<itemsPesquisaAlimentos> dados;
    String refeicao;
    String data;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getReferenciaFirebase();
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getAutenticacao();

    public adapterPeqAlmoco(Context context, List<itemsPesquisaAlimentos> dados, String refeicao,String data) {
        this.context = context;
        this.dados = dados;
        this.refeicao = refeicao;
        this.data = data;
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
                i.putExtra("REFEICAO",refeicao);
                i.putExtra("DATA",data);
                context.startActivity(i);
            }
        });

        vHolder.cardViewPesquisa.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = vHolder.getAdapterPosition();
                String codigo = dados.get(position).getCodigo();
                criaDialogoApagar(codigo);
                return true;
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


    private void criaDialogoApagar(final String codigo)
    {
        final String IDUtilizador = CodificadorBase64.codificaBase64(autenticacao.getCurrentUser().getEmail());
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Tem a certeza?");
        builder.setMessage("Deseja apagar este item da sua lista?");
        builder.setPositiveButton("Confimar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                firebaseRef.child("Refeicoes").child(IDUtilizador).child(data).child(refeicao).child(codigo).removeValue();
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
