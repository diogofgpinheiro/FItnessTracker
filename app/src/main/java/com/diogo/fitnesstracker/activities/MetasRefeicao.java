package com.diogo.fitnesstracker.activities;

import android.app.Dialog;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.diogo.fitnesstracker.R;
import com.diogo.fitnesstracker.config.ConfiguracaoFirebase;
import com.diogo.fitnesstracker.helper.CodificadorBase64;
import com.diogo.fitnesstracker.model.MetasCaloriasModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class MetasRefeicao extends AppCompatActivity implements NumberPicker.OnValueChangeListener{

    private boolean retorna = true;
    private ActionBar toolbar;

    private TextView textoPeqAlomoco,valorPeqAlmoco,textoAlmoco,valorAlmoco,textoLanche,valorLanche,textoJantar,valorJantar,textoErro;
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getAutenticacao();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getReferenciaFirebase();
    private DatabaseReference refeicaoRef;
    private ValueEventListener valueEventListenerRefeicao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metas_refeicao);
        toolbar = getSupportActionBar();
        toolbar.setTitle("Metas refeições");
        toolbar.setDisplayHomeAsUpEnabled(true);

        textoPeqAlomoco = findViewById(R.id.textPeqAlmoco);
        valorPeqAlmoco = findViewById(R.id.valorPeqAlmoco);
        textoAlmoco = findViewById(R.id.textAlmoco);
        valorAlmoco = findViewById(R.id.valorAlmoco);
        textoLanche = findViewById(R.id.textLanche);
        valorLanche = findViewById(R.id.valorLanche);
        textoJantar = findViewById(R.id.textJantar);
        valorJantar = findViewById(R.id.valorJantar);
        textoErro = findViewById(R.id.textErro);
    }


    @Override
    protected void onStart() {
        super.onStart();
        obtemDados();
        alteraDados();
    }

    public void obtemDados()
    {
        final String IDUtilizador = CodificadorBase64.codificaBase64(autenticacao.getCurrentUser().getEmail());
        refeicaoRef = firebaseRef.child("Metas").child(IDUtilizador).child("MetasNutricao");
        valueEventListenerRefeicao = refeicaoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MetasCaloriasModel refeicoes = dataSnapshot.getValue(MetasCaloriasModel.class);
                int calorias = refeicoes.getCalorias();
                double peqAlmoco  = refeicoes.getPerc_peqAlmoco();
                double almoco = refeicoes.getPerc_almoco();
                double lanche = refeicoes.getPerc_lanche();
                double jantar  = refeicoes.getPerc_jantar();

                textoPeqAlomoco.setText("Pequeno-almoço: " + (int) (calorias*peqAlmoco)+" cal");
                valorPeqAlmoco.setText((int) (peqAlmoco*100) + "%");
                textoAlmoco.setText("Almoço: " + (int) (calorias*almoco) +" cal");
                valorAlmoco.setText((int) (almoco*100) + "%");
                textoLanche.setText("Lanche: " + (int) (calorias*lanche)+" cal");
                valorLanche.setText((int) (lanche*100) + "%");
                textoJantar.setText("Jantar: " + (int) (calorias*jantar)+" cal");
                valorJantar.setText((int) (jantar*100) + "%");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void alteraDados()
    {
        valorPeqAlmoco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criaDialogo(valorPeqAlmoco);
            }
        });

        valorAlmoco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criaDialogo(valorAlmoco);
            }
        });

        valorLanche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criaDialogo(valorLanche);
            }
        });

        valorJantar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criaDialogo(valorJantar);
            }
        });
    }

    public void criaDialogo(final TextView textView)
    {
        final Dialog d = new Dialog(MetasRefeicao.this);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.number_picker_dialog);
        TextView b1 = d.findViewById(R.id.button1);
        TextView b2 = d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(100);
        np.setMinValue(0);
        np.setValue(25);
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(MetasRefeicao.this);
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                textView.setText(String.valueOf(np.getValue()+ "%"));
                validaMacros();
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();
    }

    public void validaMacros()
    {
        String IDUtilizador = CodificadorBase64.codificaBase64(autenticacao.getCurrentUser().getEmail());
        int peqAlmoico = Integer.parseInt(valorPeqAlmoco.getText().toString().replace("%",""));
        int almoco = Integer.parseInt(valorAlmoco.getText().toString().replace("%",""));
        int lanche = Integer.parseInt(valorLanche.getText().toString().replace("%",""));
        int jantar = Integer.parseInt(valorJantar.getText().toString().replace("%",""));
        if((peqAlmoico+almoco+lanche+jantar) != 100)
        {
            valorPeqAlmoco.setTextColor(Color.RED);
            valorAlmoco.setTextColor(Color.RED);
            valorLanche.setTextColor(Color.RED);
            valorJantar.setTextColor(Color.RED);
            retorna = false;
            textoErro.setVisibility(View.VISIBLE);
        }else {
            valorPeqAlmoco.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            valorAlmoco.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            valorLanche.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            valorJantar.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            retorna = true;
            textoErro.setVisibility(View.GONE);
            firebaseRef.child("Metas").child(IDUtilizador).child("MetasNutricao").child("perc_peqAlmoco").setValue((double)peqAlmoico/100);
            firebaseRef.child("Metas").child(IDUtilizador).child("MetasNutricao").child("perc_almoco").setValue((double)almoco/100);
            firebaseRef.child("Metas").child(IDUtilizador).child("MetasNutricao").child("perc_lanche").setValue((double)lanche/100);
            firebaseRef.child("Metas").child(IDUtilizador).child("MetasNutricao").child("perc_jantar").setValue((double)jantar/100);
        }
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

        Log.i("value is",""+newVal);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (retorna)
        {
            switch (item.getItemId()) {
                case android.R.id.home:
                    this.finish();
                    return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(!retorna)
        {
            return;
        }
        super.onBackPressed();
    }
}
