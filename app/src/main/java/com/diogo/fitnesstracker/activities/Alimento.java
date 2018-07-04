package com.diogo.fitnesstracker.activities;

import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.diogo.fitnesstracker.R;
import com.diogo.fitnesstracker.config.ConfiguracaoFirebase;
import com.diogo.fitnesstracker.model.Alimentos;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Alimento extends AppCompatActivity {

    private PieChart graficoAlimento;
    private Bundle extras;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getReferenciaFirebase();
    private String codigo;
    private TextView valorNome,valorQuantidade,valorUnidade,valorHidratos,valorProteinas,valorGorduras,valorMarca;
    private ActionBar toolbar;
    private Button botaoAdiciona;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alimento);

        toolbar = getSupportActionBar();
        toolbar.setTitle("Adionar Alimento");
        toolbar.setDisplayHomeAsUpEnabled(true);

        graficoAlimento = findViewById(R.id.graficoAlimento);
        valorNome = findViewById(R.id.textoNomeAlimento);
        valorQuantidade = findViewById(R.id.textoQuantidade);
        valorUnidade = findViewById(R.id.textoUnidade);
        valorHidratos = findViewById(R.id.textoHidratos);
        valorProteinas = findViewById(R.id.textoProteinas);
        valorGorduras = findViewById(R.id.textoGorduras);
        valorMarca = findViewById(R.id.textoMarca);
        botaoAdiciona = findViewById(R.id.botaoAdiciona);
        extras = getIntent().getExtras();
        carregaDados();


        valorQuantidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        valorUnidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        botaoAdiciona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    public void criaPiechart(int calorias)
    {
        graficoAlimento.setUsePercentValues(true);
        graficoAlimento.setRotationEnabled(false);
        graficoAlimento.getDescription().setEnabled(false);
        graficoAlimento.setExtraOffsets(0,10,0,5);

        graficoAlimento.setDragDecelerationEnabled(false);
        graficoAlimento.setCenterText(calorias+"\ncal");
        graficoAlimento.setCenterTextSize(12f);
        graficoAlimento.setDrawHoleEnabled(true);
        graficoAlimento.setHoleColor(Color.WHITE);
        graficoAlimento.setHoleRadius(60f);
        graficoAlimento.setEntryLabelColor(this.getResources().getColor(R.color.colorPrimary));
        graficoAlimento.setDrawEntryLabels(false);
        graficoAlimento.getLegend().setEnabled(false);

        ArrayList<PieEntry> valoresGrafico = new ArrayList<>();

        valoresGrafico.add(new PieEntry(48,"Carboidratos"));
        valoresGrafico.add(new PieEntry(27,"Proteinas"));
        valoresGrafico.add(new PieEntry(25,"Gorduras"));

        PieDataSet dataSet = new PieDataSet(valoresGrafico,"g dos macronutrientes");
        dataSet.setSliceSpace(3f);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(15f);
        data.setDrawValues(false);
        data.setValueTextColor(Color.WHITE);

        graficoAlimento.setData(data);
    }

    public void carregaDados()
    {
        if(extras != null)
        {
            codigo = extras.getString("IDAlimento");
        }

        firebaseRef.child("Alimentos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dados : dataSnapshot.getChildren())
                {
                    Alimentos alimento = dados.getValue(Alimentos.class);
                    if(dados.getKey().equals(codigo))
                    {
                        if(alimento.getMarca()!=null)
                        {
                            valorMarca.setText(("(" + alimento.getMarca() + ")"));
                            valorMarca.setVisibility(View.VISIBLE);

                        }
                        valorNome.setText(alimento.getNome());
                        valorQuantidade.setText(Double.toString(alimento.getQuantidade()));
                        valorUnidade.setText(alimento.getUnidade());
                        valorHidratos.setText(Double.toString(alimento.getCarboidratos())+ " g");
                        valorProteinas.setText(Double.toString(alimento.getProteinas())+ " g");
                        valorGorduras.setText(Double.toString(alimento.getGorduras())+ " g");
                        criaPiechart(alimento.getCalorias());
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
