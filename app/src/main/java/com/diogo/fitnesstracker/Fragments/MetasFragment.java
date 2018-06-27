package com.diogo.fitnesstracker.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.diogo.fitnesstracker.R;
import com.diogo.fitnesstracker.adapter.adapterListaMetas;
import com.diogo.fitnesstracker.adapter.adapterListaMetasNutricao;
import com.diogo.fitnesstracker.config.ConfiguracaoFirebase;
import com.diogo.fitnesstracker.helper.Calorias;
import com.diogo.fitnesstracker.helper.CodificadorBase64;
import com.diogo.fitnesstracker.model.ItemsRecycler;
import com.diogo.fitnesstracker.model.MetasPeso;
import com.diogo.fitnesstracker.model.Utilizador;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MetasFragment extends Fragment {

    private View v;


    //Firebase
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getAutenticacao();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getReferenciaFirebase();
    private DatabaseReference metasPesoRef;
    private DatabaseReference valoresRef;
    private ValueEventListener valueEventListenerMetasPeso;
    private ValueEventListener valueEventListenerMetasAltura;
    private List<ItemsRecycler> listaMetasPeso = new ArrayList<>();
    private List<ItemsRecycler> listaMetasNutricao = new ArrayList<>();

    //RecyclerView
    private RecyclerView recyclerViewMetasPeso;
    private adapterListaMetas adapterListaMetasPeso;
    private RecyclerView recyclerViewMetasNutricao;
    private adapterListaMetasNutricao adapterListaMetasNutricao;

    private TextView textView;
    private ProgressBar progressBar;
    private double valorAltura;
    private String valorGenero,valor_dataNascimento;



    public MetasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_metas, container, false);

        recyclerViewMetasPeso = v.findViewById(R.id.recyclerViewMetasPeso);
        adapterListaMetasPeso = new adapterListaMetas(getContext(),listaMetasPeso);
        recyclerViewMetasPeso.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewMetasPeso.setAdapter(adapterListaMetasPeso);
        recyclerViewMetasNutricao = v.findViewById(R.id.RecyclerViewMetasNutricao);
        adapterListaMetasNutricao = new adapterListaMetasNutricao(getContext(),listaMetasNutricao);
        recyclerViewMetasNutricao.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewMetasNutricao.setAdapter(adapterListaMetasNutricao);
        textView = v.findViewById(R.id.textView2);
        progressBar = v.findViewById(R.id.progressBar2);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        obtemDados();
    }


    public void obtemDados()
    {
        final String IDUtilizador = CodificadorBase64.codificaBase64(autenticacao.getCurrentUser().getEmail());
        metasPesoRef = firebaseRef.child("Metas").child(IDUtilizador).child("MetasPeso");
        valueEventListenerMetasPeso = metasPesoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaMetasPeso.clear();
                MetasPeso metasPeso = dataSnapshot.getValue(MetasPeso.class);
                String campoPesoInicial = Double.toString(metasPeso.getPeso_inicial());
                String campoPeso = Double.toString(metasPeso.getPeso());
                String campoMetaPeso = Double.toString(metasPeso.getMeta_peso());
                listaMetasPeso.add(new ItemsRecycler("Peso inicial",campoPesoInicial + " kg em " + metasPeso.getData_inicial()));
                listaMetasPeso.add(new ItemsRecycler("Peso",campoPeso + " kg"));
                listaMetasPeso.add(new ItemsRecycler("Meta de peso",campoMetaPeso + " kg"));
                if(metasPeso.getMeta_semanal() < 0)
                {
                    String campoMetaSemanal = Double.toString(-metasPeso.getMeta_semanal());
                    listaMetasPeso.add(new ItemsRecycler("Meta semanal","Perder " + campoMetaSemanal + " kg por semana"));
                }else if(metasPeso.getMeta_semanal() == 0)
                {
                    listaMetasPeso.add(new ItemsRecycler("Meta semanal","Manter o peso"));
                }else
                {
                    String campoMetaSemanal = Double.toString(metasPeso.getMeta_semanal());
                    listaMetasPeso.add(new ItemsRecycler("Meta semanal","Ganhar " + campoMetaSemanal + " kg por semana"));
                }
                listaMetasPeso.add(new ItemsRecycler("Nível de atividade", metasPeso.getNivel_atividade()));

                obtemValores(metasPeso.getPeso(),metasPeso.getNivel_atividade(),metasPeso.getMeta_semanal());
                adapterListaMetasPeso.notifyDataSetChanged();
                mostraLayout();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        listaMetasNutricao.clear();
        listaMetasNutricao.add(new ItemsRecycler("Metas de calorias e macronutrientes","Personalize as suas metas padrão"));
        listaMetasNutricao.add(new ItemsRecycler("Metas de calorias por refeição","Controle as calorias das suas refeições"));
    }

    public void mostraLayout()
    {
        recyclerViewMetasPeso.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);
        recyclerViewMetasNutricao.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    public void obtemValores(final double peso,final String nivel_atividade,final double meta_semanal)
    {

        final String IDUtilizador = CodificadorBase64.codificaBase64(autenticacao.getCurrentUser().getEmail());
        valoresRef = firebaseRef.child("Utilizadores").child(IDUtilizador);
        valueEventListenerMetasAltura = valoresRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Utilizador utilizador = dataSnapshot.getValue(Utilizador.class);
                valorAltura = utilizador.getAltura();
                valorGenero = utilizador.getSexo();
                valor_dataNascimento = utilizador.getData_nascimento();
                Calorias calorias = new Calorias(peso,valorAltura,valorGenero);
                int cal = calorias.calculaCalorias(nivel_atividade,meta_semanal,valor_dataNascimento);
                firebaseRef.child("Metas").child(IDUtilizador).child("MetasNutricao").child("Calorias").setValue(cal);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        metasPesoRef.removeEventListener(valueEventListenerMetasPeso);
        valoresRef.removeEventListener(valueEventListenerMetasAltura);
    }
}
