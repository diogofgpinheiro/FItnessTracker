package com.diogo.fitnesstracker.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.diogo.fitnesstracker.R;
import com.diogo.fitnesstracker.config.ConfiguracaoFirebase;
import com.diogo.fitnesstracker.helper.CodificadorBase64;
import com.diogo.fitnesstracker.model.MetasCaloriasModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class MetasCalorias extends AppCompatActivity  implements NumberPicker.OnValueChangeListener{

    private ActionBar toolbar;


    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getAutenticacao();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getReferenciaFirebase();
    private DatabaseReference caloriasRef;
    private ValueEventListener valueEventListenerCalorias;
    private boolean retorna = true;

    private TextView valorCalorias,textoCaboidratos,valorCarboidratos,textoProteinas,valorProteinas,textoGorduras,valorGorduras,textoErro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metas_calorias);
        toolbar = getSupportActionBar();
        toolbar.setTitle("Metas de calorias e macronutrientes");
        toolbar.setDisplayHomeAsUpEnabled(true);

        valorCalorias = findViewById(R.id.valorPeqAlmoco);
        textoCaboidratos = findViewById(R.id.textAlmoco);
        valorCarboidratos = findViewById(R.id.valorAlmoco);
        textoProteinas = findViewById(R.id.textLanche);
        valorProteinas = findViewById(R.id.valorLanche);
        textoGorduras = findViewById(R.id.textJantar);
        valorGorduras = findViewById(R.id.valorJantar);
        textoErro = findViewById(R.id.textErro);

    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

        Log.i("value is",""+newVal);

    }

    @Override
    protected void onStart() {
        super.onStart();
        obtemDados();
        alteraDados(valorCalorias.getText().toString());
    }

    public void obtemDados()
    {
        final String IDUtilizador = CodificadorBase64.codificaBase64(autenticacao.getCurrentUser().getEmail());
        caloriasRef = firebaseRef.child("Metas").child(IDUtilizador).child("MetasNutricao");
        valueEventListenerCalorias = caloriasRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MetasCaloriasModel metasCalorias = dataSnapshot.getValue(MetasCaloriasModel.class);
                int calorias = metasCalorias.getCalorias();
                int perc_carboidratos = (int) (metasCalorias.getPercentagem_carbo()*100);
                int perc_proteinas = (int) (metasCalorias.getPercentagem_proteina()*100);
                int perc_gorduras = (int) (metasCalorias.getPercentagem_gordura()*100);

                int carboidratos = (int) (metasCalorias.getPercentagem_carbo()*calorias)/4;
                int proteinas = (int) (metasCalorias.getPercentagem_proteina()*calorias)/4;
                int gorduras = (int) (metasCalorias.getPercentagem_gordura()*calorias)/9;
                valorCalorias.setText(Integer.toString(calorias));
                textoCaboidratos.setText("Carboidratos " + Integer.toString(carboidratos) + "g" );
                valorCarboidratos.setText(Integer.toString(perc_carboidratos) + "%");
                textoProteinas.setText("Proteínas " + Integer.toString(proteinas) + "g" );
                valorProteinas.setText(Integer.toString(perc_proteinas) + "%");
                textoGorduras.setText("Gorduras " + Integer.toString(gorduras) + "g" );
                valorGorduras.setText(Integer.toString(perc_gorduras) + "%");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void alteraDados(final String calorias)
    {
        valorCalorias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criaDialogoCalorias(calorias);
            }
        });

        valorCarboidratos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criaDialogMacros(valorCarboidratos);
            }
        });

        valorProteinas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criaDialogMacros(valorProteinas);
            }
        });

        valorGorduras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criaDialogMacros(valorGorduras);
            }
        });
    }

    public void validaMacros()
    {
        String IDUtilizador = CodificadorBase64.codificaBase64(autenticacao.getCurrentUser().getEmail());
        int carboidratos = Integer.parseInt(valorCarboidratos.getText().toString().replace("%",""));
        int proteinas = Integer.parseInt(valorProteinas.getText().toString().replace("%",""));
        int gorduras = Integer.parseInt(valorGorduras.getText().toString().replace("%",""));
        if((carboidratos+gorduras+proteinas) != 100)
        {
            valorCarboidratos.setTextColor(Color.RED);
            valorProteinas.setTextColor(Color.RED);
            valorGorduras.setTextColor(Color.RED);
            retorna = false;
            textoErro.setVisibility(View.VISIBLE);
        }else {
            valorCarboidratos.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            valorProteinas.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            valorGorduras.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            retorna = true;
            textoErro.setVisibility(View.GONE);
            firebaseRef.child("Metas").child(IDUtilizador).child("MetasNutricao").child("percentagem_carbo").setValue((double)carboidratos/100);
            firebaseRef.child("Metas").child(IDUtilizador).child("MetasNutricao").child("percentagem_proteina").setValue((double)proteinas/100);
            firebaseRef.child("Metas").child(IDUtilizador).child("MetasNutricao").child("percentagem_gordura").setValue((double)gorduras/100);
        }
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



    public void criaDialogoCalorias(String calorias) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);//obtem um layout xml para uma view
        View mView = layoutInflater.inflate(R.layout.costum_dialog, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(mView);

        final EditText editText_dialog =  mView.findViewById(R.id.editText_dialog);
        TextView textView_titulo = mView.findViewById(R.id.textview_titulo);
        TextView textView_medida = mView.findViewById(R.id.textview_medida);
        textView_titulo.setText("Calorias");
        editText_dialog.setText(calorias);
        textView_medida.setText("calorias");
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        //TODO ler dados do utilizador e guardar
                        String IDUtilizador = CodificadorBase64.codificaBase64(autenticacao.getCurrentUser().getEmail());
                        int valor = Integer.parseInt(editText_dialog.getText().toString());
                        if(valor < 1000)
                        {
                            Toast.makeText(MetasCalorias.this,"O valor das calorias tem de ser superior a 1000",Toast.LENGTH_LONG).show();
                            return;
                        }
                        firebaseRef.child("Metas").child(IDUtilizador).child("MetasNutricao").child("Calorias").setValue(valor);
                    }
                })

                .setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();                            }
                        });

        final AlertDialog dialog = alertDialogBuilder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#39796b"));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#39796b"));
            }
        });
        dialog.show();
    }

    public void criaDialogMacros(final TextView textView)
    {
        final Dialog d = new Dialog(MetasCalorias.this);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.number_picker_dialog);
        TextView b1 = d.findViewById(R.id.button1);
        TextView b2 = d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(100);
        np.setMinValue(0);
        np.setValue(50);
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(MetasCalorias.this);
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


    @Override
    protected void onStop() {
        super.onStop();
        caloriasRef.removeEventListener(valueEventListenerCalorias);
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
