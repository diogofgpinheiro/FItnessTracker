package com.diogo.fitnesstracker.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.diogo.fitnesstracker.R;
import com.diogo.fitnesstracker.activities.PesquisaAlimentos;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiarioFragment extends Fragment {

    private LinearLayout layoutAdicionaPeqAlmoco,layoutAdicionaAlmoco,layoutAdicionaLanche,layoutAdicionaJantar;
    private Intent intent;

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
        intent = new Intent(getActivity(),PesquisaAlimentos.class);
        return v;
    }

    @Override
    public void onStart() {

        listenersAdiciona();
        super.onStart();
    }


    private void listenersAdiciona()
    {
        layoutAdicionaPeqAlmoco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("REFEICAO","Pequeno-Almoço");
                startActivity(intent);
            }
        });

        layoutAdicionaAlmoco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("REFEICAO","Almoço");
                startActivity(intent);
            }
        });

        layoutAdicionaLanche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("REFEICAO","Lanche");
                startActivity(intent);
            }
        });

        layoutAdicionaJantar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("REFEICAO","Jantar");
                startActivity(intent);
            }
        });
    }
}
