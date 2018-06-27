package com.diogo.fitnesstracker.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.diogo.fitnesstracker.Manifest;
import com.diogo.fitnesstracker.R;
import com.diogo.fitnesstracker.adapter.adapterListaPerfil;
import com.diogo.fitnesstracker.config.ConfiguracaoFirebase;
import com.diogo.fitnesstracker.helper.CodificadorBase64;
import com.diogo.fitnesstracker.model.Perfil;
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
public class PerfilFragment extends Fragment {

    View v;
    private RecyclerView recyclerView;
    private adapterListaPerfil adapterListaPerfil;
    private List<Perfil> listaPerfil = new ArrayList<>();

    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getAutenticacao();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getReferenciaFirebase();
    private DatabaseReference perfilRef;
    private ValueEventListener valueEventListenerPerfil;

    private TextView dadosAltura,dadosPeso,dadosObjetivo,dadosNome,textViewAltura,textViewPeso,textViewObjetivo;
    private ImageView imageViewPerfil;
    private ProgressBar progressBar;

    public PerfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_perfil, container, false);
        dadosAltura = v.findViewById(R.id.dadosAltura);
        dadosPeso = v.findViewById(R.id.dadosPeso);
        dadosObjetivo = v.findViewById(R.id.dadosObjetivo);
        dadosNome = v.findViewById(R.id.dadosNome);
        progressBar = v.findViewById(R.id.progressBar);
        textViewAltura = v.findViewById(R.id.textViewAltura);
        textViewObjetivo = v.findViewById(R.id.textViewObjetivo);
        textViewPeso = v.findViewById(R.id.textViewPeso);
        imageViewPerfil = v.findViewById(R.id.imageView);


        recyclerView = v.findViewById(R.id.recyclerViewPerfil);
        adapterListaPerfil = new adapterListaPerfil(getContext(),listaPerfil);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapterListaPerfil);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        obtemDados();
    }

    public void obtemDados()
    {

        String IDUtilizador = CodificadorBase64.codificaBase64(autenticacao.getCurrentUser().getEmail());
        perfilRef = firebaseRef.child("Utilizadores").child(IDUtilizador);
        valueEventListenerPerfil = perfilRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaPerfil.clear();
                Utilizador utilizador = dataSnapshot.getValue(Utilizador.class);
                String campoAltura = Double.toString((int) utilizador.getAltura());
                String campoPeso = Double.toString(utilizador.getPeso());
                listaPerfil.add(new Perfil("Nome",utilizador.getNome()));
                listaPerfil.add(new Perfil("Altura",Double.toString(utilizador.getAltura()) + " cm"));
                listaPerfil.add(new Perfil("Peso",Double.toString(utilizador.getPeso()) + " kg"));
                listaPerfil.add(new Perfil("Genero",utilizador.getSexo()));
                listaPerfil.add(new Perfil("Data de nascimento",utilizador.getData_nascimento()));
                listaPerfil.add(new Perfil("E-mail",utilizador.getEmail()));
                dadosAltura.setText(campoAltura);
                dadosPeso.setText(campoPeso);
                dadosNome.setText(utilizador.getNome());
                adapterListaPerfil.notifyDataSetChanged();

                mostraLayout();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void mostraLayout()
    {
        progressBar.setVisibility(View.GONE);
        dadosAltura.setVisibility(View.VISIBLE);
        dadosPeso.setVisibility(View.VISIBLE);
        dadosObjetivo.setVisibility(View.VISIBLE);
        dadosNome.setVisibility(View.VISIBLE);
        textViewAltura.setVisibility(View.VISIBLE);
        textViewObjetivo.setVisibility(View.VISIBLE);
        textViewPeso.setVisibility(View.VISIBLE);
        imageViewPerfil.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }
}
