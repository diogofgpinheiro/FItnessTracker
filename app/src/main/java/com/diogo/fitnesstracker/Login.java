package com.diogo.fitnesstracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;



public class Login extends AppCompatActivity {


    private Button botaoSingUp;
    private Intent myIntent;

    //TODO criaçao da classe utilizador

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //TODO login do utilizador

        //TODO registo do utilizador

        // atribuiçao do botao à variável
        botaoSingUp = (Button) findViewById(R.id.botao_singup);
        // capturar clique do botao
        botaoSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Começar nova atividade
                myIntent = new Intent(Login.this,Registar.class);
                startActivity(myIntent);
            }
        });
    }

}
