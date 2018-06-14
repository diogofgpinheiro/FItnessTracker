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
import com.diogo.fitnesstracker.helper.CodificadorBase64;
import com.diogo.fitnesstracker.model.Utilizador;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class Registar3 extends AppCompatActivity {

    private EditText campoEmail, campoPassword;
    private TextView email_cima, email_baixo, password_cima, password_baixo;
    private Button botaoNext;
    private Utilizador utilizador;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registar3);
        botaoNext = findViewById(R.id.botao_proximo);

        campoEmail = findViewById(R.id.editEmail);
        campoPassword = findViewById(R.id.editPassword);

        botaoNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textoEmail = campoEmail.getText().toString();
                String textoPassword = campoPassword.getText().toString();

                if (!textoEmail.isEmpty()) {
                    if (!textoPassword.isEmpty()) {

                        utilizador = new Utilizador();
                        utilizador.setEmail(textoEmail);
                        utilizador.setPassword(textoPassword);
                        registaUtilizador();
                    } else {
                        campoPassword.setError("Por favor preencha este campo");
                    }

                } else {
                    campoEmail.setError("Por favor preencha este campo");
                }
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
                    String idUtilizador = CodificadorBase64.codificaBase64(utilizador.getEmail());
                    utilizador.setIdUtilizador(idUtilizador);
                    startActivity(new Intent(Registar3.this,Registar.class));
                    overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
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
                        Toast.makeText(Registar3.this,excessao,Toast.LENGTH_SHORT).show();
                    }
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
