package com.diogo.fitnesstracker.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import java.math.BigInteger;
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
        pesquisaRef = firebaseRef.child("Alimentos");

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
        pesquisaRef.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                listaAlimentos.clear();
                for(DataSnapshot dados:dataSnapshot.getChildren())
                {
                    Alimentos alimento = dados.getValue(Alimentos.class);
                    String nome = alimento.getNome();
                    boolean verifica = nome.toUpperCase().contains(texto);
                    if(verifica)
                    {
                        String cal = Integer.toString(alimento.getCalorias());
                        listaAlimentos.add(new itemsPesquisaAlimentos(alimento.getNome(),alimento.getMarca(),cal));
                    }
                }
                adapterListaPesquisaAlimentos.notifyDataSetChanged();
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
                final String codigo = result.getContents();
                //Toast.makeText(this,codigo,Toast.LENGTH_LONG).show();
                firebaseRef.child("Alimentos").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Boolean verifica = false;
                        Long codigo_barras = null;
                        String nome = null;
                        for(DataSnapshot dados:dataSnapshot.getChildren()) {
                            Alimentos alimento = dados.getValue(Alimentos.class);
                            if(alimento.getCodigo_barras() != null)
                            {
                                Long valor = alimento.getCodigo_barras();
                                if(valor == Long.parseLong(codigo))
                                {
                                    nome = alimento.getNome();
                                    verifica = true;
                                    codigo_barras = valor;
                                }
                            }
                        }
                        if(verifica)
                        {
                            //Todo ir para página do alimento
                            Toast.makeText(PesquisaAlimentos.this,"Sucesso " + nome,Toast.LENGTH_LONG).show();
                        }else
                        {
                            criaDialogoInsereAlimentoCodigo(codigo);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cria_alimento,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.item_criaAlimento)
        {
            startActivity(new Intent(PesquisaAlimentos.this,CriaAlimento.class));
            return true;
        }

        return true;
    }

    private void criaDialogoInsereAlimentoCodigo(final String codigo)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Lamenta-mos!");
        builder.setMessage("Este código ainda não se encontra registado. Deseja criar um novo alimento a partir dele?");
        builder.setPositiveButton("Confimar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(PesquisaAlimentos.this,CriaAlimento.class);
                i.putExtra("CODIGO_BARRAS",codigo);
                startActivity(i);
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
