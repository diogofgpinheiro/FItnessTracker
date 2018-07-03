package com.diogo.fitnesstracker.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.diogo.fitnesstracker.R;
import com.diogo.fitnesstracker.activities.Alimento;
import com.diogo.fitnesstracker.activities.PesquisaAlimentos;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiarioFragment extends Fragment {

    Button botao,botao2;


    public DiarioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_diario, container, false);

        botao = v.findViewById(R.id.button3);
        botao2 = v.findViewById(R.id.button4);

        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),PesquisaAlimentos.class));
            }
        });

        botao2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),Alimento.class));
            }
        });

        return v;
    }

}
