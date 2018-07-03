package com.diogo.fitnesstracker.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.diogo.fitnesstracker.R;
import com.diogo.fitnesstracker.config.ConfiguracaoFirebase;
import com.diogo.fitnesstracker.model.Alimentos;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class CriaAlimento extends AppCompatActivity {

    private ActionBar toolbar;
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getAutenticacao();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getReferenciaFirebase();
    private AutoCompleteTextView valorUnidade;
    private EditText valorNome,valorMarca,valorQuantidade,valorCalorias,valorCarbo,valorProt,valorGord;
    private Button botaoCriaAlimento;
    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cria_alimento);
        toolbar = getSupportActionBar();
        toolbar.setTitle("Criar alimento");
        toolbar.setDisplayHomeAsUpEnabled(true);
        valorUnidade = findViewById(R.id.editUnidade);
        valorNome = findViewById(R.id.editNomeAlimento);
        valorMarca = findViewById(R.id.editMarcaAlimento);
        valorQuantidade = findViewById(R.id.editQuantidade);
        valorCalorias = findViewById(R.id.editCalorias);
        valorCarbo = findViewById(R.id.editHidratos);
        valorProt = findViewById(R.id.editProteinas);
        valorGord = findViewById(R.id.editGorduras);
        botaoCriaAlimento = findViewById(R.id.buttonCriaAlimento);
        extras = getIntent().getExtras();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,unidades);
        valorUnidade.setAdapter(arrayAdapter);

        valorUnidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valorUnidade.showDropDown();
            }
        });

        botaoCriaAlimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textoNome = valorNome.getText().toString();
                String textoMarca = valorMarca.getText().toString();
                String textoQuantidade = valorQuantidade.getText().toString();
                String textoCalorias = valorCalorias.getText().toString();
                String textoCarbo = valorCarbo.getText().toString();
                String textoProt = valorProt.getText().toString();
                String textoGord = valorGord.getText().toString();
                String textoUnidade = valorUnidade.getText().toString();
                String codigo = null;

                if(extras != null)
                {
                    codigo = extras.getString("CODIGO_BARRAS");
                }

                if(textoNome.isEmpty())
                {
                    Toast.makeText(CriaAlimento.this,"Por favor introduza todos os dados necessários",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(textoQuantidade.isEmpty())
                {
                    Toast.makeText(CriaAlimento.this,"Por favor introduza todos os dados necessários",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(textoCalorias.isEmpty())
                {
                    Toast.makeText(CriaAlimento.this,"Por favor introduza todos os dados necessários",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(textoCarbo.isEmpty())
                {
                    Toast.makeText(CriaAlimento.this,"Por favor introduza todos os dados necessários",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(textoProt.isEmpty())
                {
                    Toast.makeText(CriaAlimento.this,"Por favor introduza todos os dados necessários",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(textoGord.isEmpty())
                {
                    Toast.makeText(CriaAlimento.this,"Por favor introduza todos os dados necessários",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(textoUnidade.isEmpty())
                {
                    Toast.makeText(CriaAlimento.this,"Por favor introduza todos os dados necessários",Toast.LENGTH_SHORT).show();
                    return;
                }

                int calorias = Integer.parseInt(textoCalorias);
                double hidratos = Double.parseDouble(textoCarbo);
                double proteinas =Double.parseDouble(textoProt);
                double gorduras = Double.parseDouble(textoGord);
                double soma = hidratos*4 + proteinas*4 + gorduras*9;


                Alimentos conteudoAlimento = new Alimentos();
                conteudoAlimento.setNome(textoNome);
                if(textoMarca!=null) {
                    conteudoAlimento.setMarca(textoMarca);
                }
                if(codigo != null)
                {
                    conteudoAlimento.setCodigo_barras(Long.parseLong(codigo));
                }
                conteudoAlimento.setQuantidade(Double.parseDouble(textoQuantidade));
                conteudoAlimento.setUnidade(textoUnidade);
                conteudoAlimento.setCalorias(calorias);
                conteudoAlimento.setCarboidratos(hidratos);
                conteudoAlimento.setProteinas(proteinas);
                conteudoAlimento.setGorduras(gorduras);
                if(soma>calorias-30 && soma<calorias+30) {
                    conteudoAlimento.gravar();
                    finish();
                }else {
                    Toast.makeText(CriaAlimento.this,"Por favor introduza os valores corretos nos macronutrientes",Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    private static final String[] unidades = new String[]{"gramas","colher de chá","colher de sopa","bolacha"};


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
