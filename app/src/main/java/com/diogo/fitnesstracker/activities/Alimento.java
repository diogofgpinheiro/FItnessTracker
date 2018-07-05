package com.diogo.fitnesstracker.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.diogo.fitnesstracker.R;
import com.diogo.fitnesstracker.config.ConfiguracaoFirebase;
import com.diogo.fitnesstracker.helper.CodificadorBase64;
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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class Alimento extends AppCompatActivity {

    private PieChart graficoAlimento;
    private Bundle extras;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getReferenciaFirebase();
    private String codigo;
    private TextView valorNome,valorQuantidade,valorUnidade,valorHidratos,valorProteinas,valorGorduras,valorMarca;
    private ActionBar toolbar;
    private Button botaoAdiciona;
    private String refeicao,data,quantidadeAlimento;
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getAutenticacao();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alimento);

        toolbar = getSupportActionBar();
        toolbar.setTitle("Adicionar Alimento");
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
                criaDialogo();

            }
        });


        valorUnidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criaDialogo();

            }
        });

        botaoAdiciona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(extras != null)
                {
                    refeicao = extras.getString("REFEICAO");
                    codigo = extras.getString("IDAlimento");
                    data = extras.getString("DATA");

                }

                String IDUtilizador = CodificadorBase64.codificaBase64(autenticacao.getCurrentUser().getEmail());
                Alimentos alimento = new Alimentos();
                alimento.setNome(valorNome.getText().toString());
                alimento.setQuantidade(Double.parseDouble(valorQuantidade.getText().toString()));
                if(!valorMarca.getText().toString().trim().equals(""))
                {
                    alimento.setMarca(valorMarca.getText().toString());
                }
                alimento.setUnidade(valorUnidade.getText().toString());
                alimento.setCalorias(Integer.parseInt(graficoAlimento.getCenterText().toString().replace("cal","").trim()));
                alimento.setCarboidratos(Double.parseDouble(valorHidratos.getText().toString().replace("g","").trim()));
                alimento.setProteinas(Double.parseDouble(valorProteinas.getText().toString().replace("g","").trim()));
                alimento.setGorduras(Double.parseDouble(valorGorduras.getText().toString().replace("g","").trim()));
                alimento.adicionaRefeicao(IDUtilizador,codigo,refeicao,data);
                startActivity(new Intent(Alimento.this,PaginaPrincipal.class));
                finish();
            }
        });
    }


    public void criaPiechart(int calorias,float hidratos,float prot,float gord)
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

        valoresGrafico.add(new PieEntry(hidratos,"Carboidratos"));
        valoresGrafico.add(new PieEntry(prot,"Proteinas"));
        valoresGrafico.add(new PieEntry(gord,"Gorduras"));

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
                    double quant;
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
                        float percentagemHidratos = (float) (alimento.getCarboidratos()*100/alimento.getQuantidade());
                        float percentagemProt = (float) (alimento.getProteinas()*100/alimento.getQuantidade());
                        float percentagemGord =(float) (alimento.getGorduras()*100/alimento.getQuantidade());
                        criaPiechart(alimento.getCalorias(),percentagemHidratos,percentagemProt,percentagemGord);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void criaDialogo()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.dialogo_alimentos,null);
        builder.setTitle("Quantidade");
        Spinner spinner = mView.findViewById(R.id.spinnerLista);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,unidades);
        spinner.setAdapter(adapter);
        final EditText editQuantidadeAlimento = mView.findViewById(R.id.editQuantidadeAlimento);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String valor = editQuantidadeAlimento.getText().toString();
                double quantidadeInicial = Double.parseDouble(valorQuantidade.getText().toString());
                double hidratosInicial = Double.parseDouble(valorHidratos.getText().toString().replace("g","").trim());
                double proteinasInicial = Double.parseDouble(valorProteinas.getText().toString().replace("g","").trim());
                double gordurasIncial = Double.parseDouble(valorGorduras.getText().toString().replace("g","").trim());
                int caloriasInicial = Integer.parseInt(graficoAlimento.getCenterText().toString().replace("cal","").trim());
                if(!valor.trim().equals(""))
                {
                    double quantidadeAtual = Double.parseDouble(valor);
                    float hidratosAtual = (float) (hidratosInicial*quantidadeAtual/quantidadeInicial);
                    float proteinasAtual = (float) (proteinasInicial*quantidadeAtual/quantidadeInicial);
                    float gordurasAtual = (float) (gordurasIncial*quantidadeAtual/quantidadeInicial);
                    int caloriasAtual = (int) (caloriasInicial*quantidadeAtual/quantidadeInicial);
                    valorQuantidade.setText(valor);

                    NumberFormat formatter = NumberFormat.getNumberInstance();
                    formatter.setMinimumFractionDigits(0);
                    formatter.setMaximumFractionDigits(2);

                    valorHidratos.setText((formatter.format(hidratosAtual) + "g").replaceAll(",","."));
                    valorProteinas.setText((formatter.format(proteinasAtual) + "g").replaceAll(",","."));
                    valorGorduras.setText((formatter.format(gordurasAtual) + "g").replaceAll(",","."));
                    criaPiechart(caloriasAtual,hidratosAtual,proteinasAtual,gordurasAtual);
                }else {
                    Toast.makeText(Alimento.this,"Por favor introduza os dados corretamente",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
         builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 dialog.cancel();
             }
         });

         builder.setView(mView);
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

    private static final String[] unidades = new String[]{"gramas"};

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
