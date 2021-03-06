package com.diogo.fitnesstracker.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.diogo.fitnesstracker.R;
import com.diogo.fitnesstracker.activities.Login;
import com.diogo.fitnesstracker.config.ConfiguracaoFirebase;
import com.google.firebase.auth.FirebaseAuth;

import java.security.Permission;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        verificarUtilizadorLogado();

    }

    public void verificarUtilizadorLogado()
    {
        //obtem instancia da autenticação da firebase
        autenticacao = ConfiguracaoFirebase.getAutenticacao();
        //verifica se o utilizador está logado ou não
        if(autenticacao.getCurrentUser() != null)
        {
            startActivity(new Intent(this,PaginaPrincipal.class));
            finish();
        }else {
            startActivity(new Intent(this,Login.class));
            finish();
        }
    }
}
