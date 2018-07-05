package com.diogo.fitnesstracker.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.diogo.fitnesstracker.R;
import com.diogo.fitnesstracker.config.ConfiguracaoFirebase;
import com.diogo.fitnesstracker.helper.Calorias;
import com.diogo.fitnesstracker.helper.CodificadorBase64;
import com.diogo.fitnesstracker.model.MetasCaloriasModel;
import com.diogo.fitnesstracker.model.MetasPeso;
import com.diogo.fitnesstracker.model.Utilizador;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Registar3 extends AppCompatActivity {

    private EditText campoEmail, campoPassword;
    private Button botaoNext;
    private Utilizador utilizador;
    private MetasPeso metasPeso;
    private MetasCaloriasModel metaCalorias;
    private FirebaseAuth autenticacao;
    private DatabaseReference firebaseRef;
    private Bundle extras;
    private String textoNome,textoGenero,textoData,textoAltura,textoPeso,textoAtividade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registar3);
        botaoNext = findViewById(R.id.botao_proximo);
        extras = getIntent().getExtras();

        campoEmail = findViewById(R.id.editEmail);
        campoPassword = findViewById(R.id.editPassword);

        botaoNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textoEmail = campoEmail.getText().toString();
                String textoPassword = campoPassword.getText().toString();

                if(textoEmail.isEmpty())
                {
                    campoEmail.setError("Por favor preencha este campo");
                    return;
                }

                if(textoPassword.isEmpty())
                {
                    campoPassword.setError("Por favor preencha este campo");
                    return;
                }

                utilizador = new Utilizador();
                metasPeso = new MetasPeso();
                metaCalorias = new MetasCaloriasModel();
                utilizador.setEmail(textoEmail);
                utilizador.setPassword(textoPassword);
                registaUtilizador();
            }
        });

    }

    public void registaUtilizador()
    {
        autenticacao = ConfiguracaoFirebase.getAutenticacao();

        autenticacao.createUserWithEmailAndPassword(utilizador.getEmail(),utilizador.getPassword()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful())
                {
                    Date currentTime = Calendar.getInstance().getTime();
                    SimpleDateFormat dataformat = new SimpleDateFormat("dd/MM/yyyy");
                    String data = dataformat.format(currentTime);
                    String idUtilizador = CodificadorBase64.codificaBase64(utilizador.getEmail());

                    if(extras != null)
                    {
                        textoNome = extras.getString("NOME");
                        textoData = extras.getString("DATA");
                        textoGenero = extras.getString("GENERO");
                        textoAltura = extras.getString("ALTURA");
                        textoPeso = extras.getString("PESO");
                        textoAtividade = extras.getString("ATIVIDADE");
                    }

                    utilizador.setNome(textoNome);
                    utilizador.setSexo(textoGenero);
                    utilizador.setData_nascimento(textoData);
                    utilizador.setAltura(Double.parseDouble(textoAltura));
                    utilizador.setPeso(Double.parseDouble(textoPeso));
                    utilizador.setAtividade(textoAtividade);
                    utilizador.setIdUtilizador(idUtilizador);
                    utilizador.setData_criacao(data);
                    utilizador.gravar();
                    metasPeso.setPeso_inicial(Double.parseDouble(textoPeso));
                    metasPeso.setPeso(Double.parseDouble(textoPeso));
                    metasPeso.setMeta_peso(Double.parseDouble(textoPeso));
                    metasPeso.setMeta_semanal(0);
                    metasPeso.setNivel_atividade(textoAtividade);
                    metasPeso.setData_inicial(data);
                    metasPeso.gravar(idUtilizador);
                    metaCalorias.setPerc_almoco(0.25);
                    metaCalorias.setPerc_peqAlmoco(0.25);
                    metaCalorias.setPerc_lanche(0.25);
                    metaCalorias.setPerc_jantar(0.25);
                    metaCalorias.setPercentagem_carbo(0.5);
                    metaCalorias.setPercentagem_gordura(0.2);
                    metaCalorias.setPercentagem_proteina(0.3);
                    Calorias calc = new Calorias(Double.parseDouble(textoPeso),Double.parseDouble(textoAltura),textoGenero);
                    int calorias = calc.calculaCalorias(textoAtividade,0,data);
                    metaCalorias.setCalorias(calorias);
                    metaCalorias.gravar(idUtilizador);
                    Intent i = new Intent(Registar3.this,PaginaPrincipal.class);
                    startActivity(i);
                    finish();
                }else {
                    String excessao = "";
                    try
                    {
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e)
                    {
                        excessao = "Digite uma senha mais forte!";
                        campoPassword.setError(excessao);
                    }catch (FirebaseAuthInvalidCredentialsException e)
                    {
                        excessao = "Digite um email válido por favor!";
                        campoEmail.setError(excessao);
                    }catch (FirebaseAuthUserCollisionException e)
                    {
                        excessao = "Esta conta já existe!";
                        campoEmail.setError(excessao);
                    }catch (Exception e)
                    {
                        excessao = "Erro ao registar o utilizador" + e.getMessage();
                    }
                    Toast.makeText(Registar3.this,excessao,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_dir,R.anim.slide_out_dir);
    }
}
