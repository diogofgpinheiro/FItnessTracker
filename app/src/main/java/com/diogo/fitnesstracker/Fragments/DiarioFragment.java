package com.diogo.fitnesstracker.Fragments;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.diogo.fitnesstracker.R;
import com.diogo.fitnesstracker.activities.PesquisaAlimentos;
import com.diogo.fitnesstracker.adapter.adapterJantar;
import com.diogo.fitnesstracker.adapter.adapterLanche;
import com.diogo.fitnesstracker.adapter.adapterPeqAlmoco;
import com.diogo.fitnesstracker.adapter.adpterAlmoco;
import com.diogo.fitnesstracker.config.ConfiguracaoFirebase;
import com.diogo.fitnesstracker.helper.CodificadorBase64;
import com.diogo.fitnesstracker.model.Alimentos;
import com.diogo.fitnesstracker.model.MetasCaloriasModel;
import com.diogo.fitnesstracker.model.itemsPesquisaAlimentos;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiarioFragment extends Fragment {

    private LinearLayout layoutAdicionaPeqAlmoco,layoutAdicionaAlmoco,layoutAdicionaLanche,layoutAdicionaJantar;
    private TextView textViewData;
    private Intent intent;
    private ImageView diaSeguinte,diaAnterior;
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getAutenticacao();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getReferenciaFirebase();
    private DatabaseReference peqAlmocoRef,almocoRef,lancheRef,jantarRef;
    private ValueEventListener valueEventListenerPeqAlmoco,valueEventListenerAlmoco,valueEventListenerLanche,valueEventListenerJantar;
    private TextView caloriasTotais,caloriasGastas,caloriasRestantes;
    private TextView caloriasPeqAlmoco,caloriasAlmoco,caloriasLanche,caloriasJantar;
    private TextView caloriasMetaPeq,caloriasMetaAlmoco,caloriasMetaLanche,caloriasMetaJantar;
    private TextView hidratosPeqAlmoco,proteinasPeqAlmoco,gordurasPeqAlmoco;
    private TextView hidratosAlmoco,proteinasAlmoco,gordurasAlmoco;
    private TextView hidratosLanche,proteinasLanche,gordurasLanche;
    private TextView hidratosJantar,proteinasJantar,gordurasJantar;

    private RecyclerView recyclerPeqAlmoco,recyclerAlmoco,recyclerLanche,recyclerJantar;
    private adapterPeqAlmoco adapterPeqAlmoco;
    private adpterAlmoco adapterAlmoco;
    private adapterLanche adapterLanche;
    private adapterJantar adapterJantar;
    private List<itemsPesquisaAlimentos> listaPeqAlmoco = new ArrayList<>();
    private List<itemsPesquisaAlimentos> listaAlmoco = new ArrayList<>();
    private List<itemsPesquisaAlimentos> listaLanche = new ArrayList<>();
    private List<itemsPesquisaAlimentos> listaJantar = new ArrayList<>();
    private ScrollView scrollView;
    private LinearLayout layoutDelay;


    public DiarioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_diario, container, false);

        layoutAdicionaPeqAlmoco = v.findViewById(R.id.layoutAdicionaPeqAlmoco);
        layoutAdicionaAlmoco = v.findViewById(R.id.layoutAdicionaAlmoco);
        layoutAdicionaLanche = v.findViewById(R.id.layoutAdicionaLanche);
        layoutAdicionaJantar = v.findViewById(R.id.layoutAdicionaJantar);
        textViewData = v.findViewById(R.id.dia);
        diaSeguinte = v.findViewById(R.id.diaSeguinte);
        diaAnterior = v.findViewById(R.id.diaAnterior);
        caloriasTotais = v.findViewById(R.id.caloriasTotais);
        caloriasGastas = v.findViewById(R.id.caloriasGastas);
        caloriasRestantes = v.findViewById(R.id.caloriasRestantes);
        caloriasPeqAlmoco = v.findViewById(R.id.caloriasPeqAlmoco);
        caloriasAlmoco = v.findViewById(R.id.caloriasAlmoco);
        caloriasLanche = v.findViewById(R.id.caloriasLanche);
        caloriasJantar = v.findViewById(R.id.caloriasJantar);
        recyclerPeqAlmoco = v.findViewById(R.id.recyclerPeqAlmoco);
        recyclerAlmoco = v.findViewById(R.id.recyclerAlmoco);
        recyclerLanche = v.findViewById(R.id.recyclerLanche);
        recyclerJantar = v.findViewById(R.id.recyclerJantar);
        caloriasMetaPeq = v.findViewById(R.id.caloriasPeqAlmocoMeta);
        caloriasMetaAlmoco = v.findViewById(R.id.caloriasAlmocoMeta);
        caloriasMetaLanche = v.findViewById(R.id.caloriasLancheMeta);
        caloriasMetaJantar = v.findViewById(R.id.caloriasJantarMeta);
        scrollView = v.findViewById(R.id.layoutPrincipal);
        layoutDelay = v.findViewById(R.id.layoutDelay);
        hidratosPeqAlmoco = v.findViewById(R.id.hidratosPeqAlmoco);
        proteinasPeqAlmoco = v.findViewById(R.id.ProteinasPeqAlmoco);
        gordurasPeqAlmoco = v.findViewById(R.id.GordurasPeqAlmoco);

        hidratosAlmoco = v.findViewById(R.id.hidratosAlmoco);
        proteinasAlmoco = v.findViewById(R.id.proteinasAlmoco);
        gordurasAlmoco = v.findViewById(R.id.gordurasAlmoco);

        hidratosLanche = v.findViewById(R.id.hidratosLanche);
        proteinasLanche = v.findViewById(R.id.proteinasLanche);
        gordurasLanche = v.findViewById(R.id.gordurasLanche);

        hidratosJantar = v.findViewById(R.id.hidratosJantar);
        proteinasJantar = v.findViewById(R.id.proteinasJantar);
        gordurasJantar = v.findViewById(R.id.gordurasJantar);

        intent = new Intent(getActivity(),PesquisaAlimentos.class);
        return v;
    }

    @Override
    public void onStart() {
        scrollView.setVisibility(View.GONE);
        layoutDelay.setVisibility(View.VISIBLE);
        listenersAdiciona();
        carregaData();
        carregaCalorias();
        carregaRecyclers();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                layoutDelay.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
            }
        }, 500);
        super.onStart();
    }

    private void listenersAdiciona()
    {
        layoutAdicionaPeqAlmoco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("REFEICAO","Pequeno-Almoço");
                intent.putExtra("DATA",textViewData.getText().toString());
                startActivity(intent);
            }
        });

        layoutAdicionaAlmoco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("REFEICAO","Almoço");
                intent.putExtra("DATA",textViewData.getText().toString());
                startActivity(intent);
            }
        });

        layoutAdicionaLanche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("REFEICAO","Lanche");
                intent.putExtra("DATA",textViewData.getText().toString());
                startActivity(intent);
            }
        });

        layoutAdicionaJantar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("REFEICAO","Jantar");
                intent.putExtra("DATA",textViewData.getText().toString());
                startActivity(intent);
            }
        });
    }


    private void carregaData()
    {
        final Date data = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String textoData = df.format(data);
        textViewData.setText(textoData);


        diaSeguinte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.setVisibility(View.GONE);
                layoutDelay.setVisibility(View.VISIBLE);
                String dt = textViewData.getText().toString();
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                Calendar c = Calendar.getInstance();
                try {
                    c.setTime(df.parse(dt));
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                c.add(Calendar.DATE,1);
                dt = df.format(c.getTime());
                textViewData.setText(dt);
                carregaRecyclers();
                carregaCalorias();

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        layoutDelay.setVisibility(View.GONE);
                        scrollView.setVisibility(View.VISIBLE);
                    }
                }, 500);
            }
        });

        diaAnterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.setVisibility(View.GONE);
                layoutDelay.setVisibility(View.VISIBLE);
                String dt = textViewData.getText().toString();
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                Calendar c = Calendar.getInstance();
                try {
                    c.setTime(df.parse(dt));
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                c.add(Calendar.DATE,-1);
                dt = df.format(c.getTime());
                textViewData.setText(dt);
                carregaRecyclers();
                carregaCalorias();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        layoutDelay.setVisibility(View.GONE);
                        scrollView.setVisibility(View.VISIBLE);
                    }
                }, 500);
            }
        });
    }
    private void carregaCalorias()
    {

        String IDUtilizador = CodificadorBase64.codificaBase64(autenticacao.getCurrentUser().getEmail());

        firebaseRef.child("Metas").child(IDUtilizador).child("MetasNutricao").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MetasCaloriasModel metas = dataSnapshot.getValue(MetasCaloriasModel.class);
                String calorias = Integer.toString(metas.getCalorias());
                int calGastas = Integer.parseInt(caloriasPeqAlmoco.getText().toString()) + Integer.parseInt(caloriasAlmoco.getText().toString()) +Integer.parseInt(caloriasLanche.getText().toString()) +Integer.parseInt(caloriasJantar.getText().toString());
                int calRes = (metas.getCalorias() - calGastas);
                caloriasTotais.setText(calorias);
                caloriasRestantes.setText(Integer.toString(calRes));
                caloriasGastas.setText(Integer.toString(calGastas));
                if (calRes < 0)
                {
                    caloriasRestantes.setTextColor(Color.RED);
                }else
                {
                    caloriasRestantes.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
                int calPeq = (int) (metas.getCalorias()*metas.getPerc_peqAlmoco());
                int calAlm = (int) (metas.getCalorias()*metas.getPerc_almoco());
                int calLan = (int) (metas.getCalorias()*metas.getPerc_lanche());
                int calJan = (int) (metas.getCalorias()*metas.getPerc_jantar());
                caloriasMetaPeq.setText(Integer.toString(calPeq));
                caloriasMetaAlmoco.setText(Integer.toString(calAlm));
                caloriasMetaLanche.setText(Integer.toString(calLan));
                caloriasMetaJantar.setText(Integer.toString(calJan));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void carregaRecyclers()
    {
        adapterPeqAlmoco = new adapterPeqAlmoco(getActivity(), listaPeqAlmoco,"Pequeno-Almoço",textViewData.getText().toString());
        recyclerPeqAlmoco.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerPeqAlmoco.setAdapter(adapterPeqAlmoco);

        adapterAlmoco = new adpterAlmoco(getActivity(), listaAlmoco,"Almoço",textViewData.getText().toString());
        recyclerAlmoco.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerAlmoco.setAdapter(adapterAlmoco);

        adapterLanche = new adapterLanche(getActivity(), listaLanche,"Lanche",textViewData.getText().toString());
        recyclerLanche.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerLanche.setAdapter(adapterLanche);


        adapterJantar = new adapterJantar(getActivity(), listaJantar,"Jantar",textViewData.getText().toString());
        recyclerJantar.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerJantar.setAdapter(adapterJantar);

        listaPeqAlmoco.clear();
        caloriasPeqAlmoco.setText("0");
        String IDUtilizador = CodificadorBase64.codificaBase64(autenticacao.getCurrentUser().getEmail());
        peqAlmocoRef = firebaseRef.child("Refeicoes").child(IDUtilizador).child(textViewData.getText().toString()).child("Pequeno-Almoço");
        valueEventListenerPeqAlmoco = peqAlmocoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int calPeqAlmoco = 0;
                double h = 0;
                double p = 0;
                double g = 0;

                listaPeqAlmoco.clear();
                for (DataSnapshot dados: dataSnapshot.getChildren())
                {
                    Alimentos alimento = dados.getValue(Alimentos.class);
                    String cal = Integer.toString(alimento.getCalorias());
                    listaPeqAlmoco.add(new itemsPesquisaAlimentos(alimento.getNome()+ " " + alimento.getMarca(),alimento.getQuantidade()+" "+alimento.getUnidade(),cal,dados.getKey()));
                    calPeqAlmoco = calPeqAlmoco + alimento.getCalorias();
                    h = h+ alimento.getCarboidratos();
                    p = p + alimento.getProteinas();
                    g = g + alimento.getGorduras();
                }

                caloriasPeqAlmoco.setText(Integer.toString(calPeqAlmoco));
                hidratosPeqAlmoco.setText(Double.toString(h));
                proteinasPeqAlmoco.setText(Double.toString(p));
                gordurasPeqAlmoco.setText(Double.toString(g));
                carregaCalorias();
                adapterPeqAlmoco.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listaAlmoco.clear();
        caloriasAlmoco.setText("0");
        almocoRef = firebaseRef.child("Refeicoes").child(IDUtilizador).child(textViewData.getText().toString()).child("Almoço");
        valueEventListenerAlmoco = almocoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int calAlmoco = 0;
                double h = 0;
                double p = 0;
                double g = 0;

                listaAlmoco.clear();
                for (DataSnapshot dados: dataSnapshot.getChildren())
                {
                    Alimentos alimento = dados.getValue(Alimentos.class);
                    String cal = Integer.toString(alimento.getCalorias());
                    listaAlmoco.add(new itemsPesquisaAlimentos(alimento.getNome()+ " " + alimento.getMarca(),alimento.getQuantidade()+" "+alimento.getUnidade(),cal,dados.getKey()));
                    calAlmoco = calAlmoco + alimento.getCalorias();
                    h = h+ alimento.getCarboidratos();
                    p = p + alimento.getProteinas();
                    g = g + alimento.getGorduras();
                }

                caloriasAlmoco.setText(Integer.toString(calAlmoco));
                hidratosAlmoco.setText(Double.toString(h));
                proteinasAlmoco.setText(Double.toString(p));
                gordurasAlmoco.setText(Double.toString(g));
                adapterAlmoco.notifyDataSetChanged();
                carregaCalorias();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listaLanche.clear();
        caloriasLanche.setText("0");
        lancheRef = firebaseRef.child("Refeicoes").child(IDUtilizador).child(textViewData.getText().toString()).child("Lanche");
        valueEventListenerLanche = lancheRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int calLanche = 0;
                double h = 0;
                double p = 0;
                double g = 0;
                listaLanche.clear();
                for (DataSnapshot dados: dataSnapshot.getChildren())
                {
                    Alimentos alimento = dados.getValue(Alimentos.class);
                    String cal = Integer.toString(alimento.getCalorias());
                    listaLanche.add(new itemsPesquisaAlimentos(alimento.getNome()+ " " + alimento.getMarca(),alimento.getQuantidade()+" "+alimento.getUnidade(),cal,dados.getKey()));
                    calLanche = calLanche + alimento.getCalorias();
                    h = h+ alimento.getCarboidratos();
                    p = p + alimento.getProteinas();
                    g = g + alimento.getGorduras();
                }

                caloriasLanche.setText(Integer.toString(calLanche));
                hidratosLanche.setText(Double.toString(h));
                proteinasLanche.setText(Double.toString(p));
                gordurasLanche.setText(Double.toString(g));
                adapterLanche.notifyDataSetChanged();
                carregaCalorias();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listaJantar.clear();
        caloriasJantar.setText("0");
        jantarRef = firebaseRef.child("Refeicoes").child(IDUtilizador).child(textViewData.getText().toString()).child("Jantar");
        valueEventListenerJantar = jantarRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int calJantar = 0;
                double h = 0;
                double p = 0;
                double g = 0;
                listaJantar.clear();
                for (DataSnapshot dados: dataSnapshot.getChildren())
                {
                    Alimentos alimento = dados.getValue(Alimentos.class);
                    String cal = Integer.toString(alimento.getCalorias());
                    listaJantar.add(new itemsPesquisaAlimentos(alimento.getNome()+ " " + alimento.getMarca(),alimento.getQuantidade()+" "+alimento.getUnidade(),cal,dados.getKey()));
                    calJantar = calJantar + alimento.getCalorias();
                    h = h + alimento.getCarboidratos();
                    p = p + alimento.getProteinas();
                    g = g + alimento.getGorduras();
                }
                caloriasJantar.setText(Integer.toString(calJantar));
                hidratosJantar.setText(Double.toString(h));
                proteinasJantar.setText(Double.toString(p));
                gordurasJantar.setText(Double.toString(g));
                adapterJantar.notifyDataSetChanged();
                carregaCalorias();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }





    @Override
    public void onStop() {
        peqAlmocoRef.removeEventListener(valueEventListenerPeqAlmoco);
        almocoRef.removeEventListener(valueEventListenerAlmoco);
        lancheRef.removeEventListener(valueEventListenerLanche);
        jantarRef.removeEventListener(valueEventListenerJantar);
        super.onStop();
    }
}
