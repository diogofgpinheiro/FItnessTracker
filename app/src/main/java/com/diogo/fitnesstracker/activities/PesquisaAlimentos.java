package com.diogo.fitnesstracker.activities;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.diogo.fitnesstracker.R;
import com.diogo.fitnesstracker.config.ConfiguracaoFirebase;
import com.diogo.fitnesstracker.model.Alimentos;
import com.diogo.fitnesstracker.model.itemsPesquisaAlimentos;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import com.diogo.fitnesstracker.adapter.adapterListaPesquisaAlimentos;



public class PesquisaAlimentos extends AppCompatActivity {


    private SearchView searchPesquisa;
    private RecyclerView recyclerPesquisa;
    private List<itemsPesquisaAlimentos> listaAlimentos = new ArrayList<>();
    private adapterListaPesquisaAlimentos adapterListaPesquisaAlimentos;


    private ImageView imagemScanner;

    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getAutenticacao();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getReferenciaFirebase();
    private DatabaseReference pesquisaRef;
    private ValueEventListener valueEventListenerPesquisa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisa_alimentos);

        imagemScanner = findViewById(R.id.imagemBarcode);

        imagemScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(PesquisaAlimentos.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.PRODUCT_CODE_TYPES);
                integrator.setPrompt("Coloque o código no centro da câmera");
                integrator.setCameraId(0);
                integrator.setOrientationLocked(true);
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(false);
                integrator.setCaptureActivity(CaptureActivityPortrait.class);
                integrator.initiateScan();

            }
        });

        searchPesquisa = findViewById(R.id.searchViewPesquisa);
        recyclerPesquisa = findViewById(R.id.recyclerPesquisa);

        searchPesquisa.setQueryHint("Procurar alimentos");
        searchPesquisa.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(PesquisaAlimentos.this,query,Toast.LENGTH_LONG).show();
                String textoInserido = query.toUpperCase();
                if(!textoInserido.trim().equals("")) {
                    procuraAlimentos(textoInserido);
                }else {
                    listaAlimentos.clear();
                    adapterListaPesquisaAlimentos.notifyDataSetChanged();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String textoInserido = newText.toUpperCase();
                if(!textoInserido.trim().equals("")) {
                    procuraAlimentos(textoInserido);
                }else {
                    listaAlimentos.clear();
                    adapterListaPesquisaAlimentos.notifyDataSetChanged();
                }
                return true;
            }
        });

        recyclerPesquisa = findViewById(R.id.recyclerPesquisa);
        adapterListaPesquisaAlimentos = new adapterListaPesquisaAlimentos(this,listaAlimentos);
        recyclerPesquisa.setLayoutManager(new LinearLayoutManager(this));
        recyclerPesquisa.setAdapter(adapterListaPesquisaAlimentos);
    }


    private void procuraAlimentos(final String texto)
    {
        pesquisaRef = firebaseRef.child("Alimentos");
        valueEventListenerPesquisa = pesquisaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot dados:dataSnapshot.getChildren())
                {
                    listaAlimentos.clear();
                    Alimentos alimento = dados.getValue(Alimentos.class);
                    String nome = alimento.getNome();
                    boolean verifica = nome.toUpperCase().contains(texto);
                    if(verifica)
                    {
                        Toast.makeText(PesquisaAlimentos.this,nome,Toast.LENGTH_LONG).show();
                        String cal = Integer.toString(alimento.getCalorias());
                        listaAlimentos.add(new itemsPesquisaAlimentos(alimento.getNome(),alimento.getMarca(),cal));
                    }
                    adapterListaPesquisaAlimentos.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result != null)
        {
            if(result.getContents()==null)
            {
                Toast.makeText(this,"Cancelou",Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this,result.getContents(),Toast.LENGTH_LONG).show();
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        pesquisaRef.removeEventListener(valueEventListenerPesquisa);
    }
}
