package com.diogo.fitnesstracker.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.diogo.fitnesstracker.R;
import com.diogo.fitnesstracker.config.ConfiguracaoFirebase;
import com.diogo.fitnesstracker.model.Utilizador;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;


public class Login extends AppCompatActivity {


    private Button botaoSingUp,botaoLogin;
    private Utilizador utilizador;
    private FirebaseAuth autenticacao;
    private EditText campoEmail,campoPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // atribuiçao do botao à variável
        botaoSingUp = findViewById(R.id.botao_singup);
        botaoLogin = findViewById(R.id.botaoLogin);
        campoEmail = findViewById(R.id.editEmail);
        campoPassword = findViewById(R.id.editPassword);
        // capturar clique do botao
        botaoSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Começar nova atividade
                startActivity(new Intent(Login.this,Registar.class));
                overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
            }
        });

        botaoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textoEmail = campoEmail.getText().toString();
                String textoPassword = campoPassword.getText().toString();

                if(!textoEmail.isEmpty())
                {
                    if(!textoPassword.isEmpty())
                    {
                        utilizador = new Utilizador();
                        utilizador.setEmail(textoEmail);
                        utilizador.setPassword(textoPassword);
                        validaLogin();
                    }
                }
            }
        });
    }

    public void validaLogin()
    {
        autenticacao = ConfiguracaoFirebase.getAutenticacao();
        autenticacao.signInWithEmailAndPassword(utilizador.getEmail(),utilizador.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful())
                {
                    //abre activity principal
                    startActivity(new Intent(Login.this,PaginaPrincipal.class));
                    finish();

                }else
                {
                    String excessao = "";
                    try
                    {
                        throw task.getException();
                    }catch (FirebaseAuthInvalidCredentialsException e)
                    {
                        excessao = "Por favor introduza uma password válida!";
                    }catch (FirebaseAuthInvalidUserException e)
                    {
                        excessao = "Este utilizador não se encontra registado!";
                    }catch (Exception e)
                    {
                        excessao = "Erro ao efetuar o login";
                        e.printStackTrace();
                    }
                    Toast.makeText(Login.this,excessao,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
