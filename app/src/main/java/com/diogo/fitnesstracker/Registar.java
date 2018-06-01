package com.diogo.fitnesstracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Registar extends AppCompatActivity {

    private EditText editText_email;
    private EditText editText_password;
    private TextView email_cima, email_baixo, password_cima, password_baixo;
    private Animation fromSide;
    private Button botaoNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registar);
        botaoNext = (Button) findViewById(R.id.botao_proximo);
        botaoNext.setEnabled(false);
        // Insere animaçoes
        insereAnimacoes();
        verificaMudancaEditText();
        //TODO verificar se os dados estao corretos e ativar botao next e muda-lo de cor
        //TODO passar para a proxima pagina ao clicar no botao
        //TODO passar para a proxima pagina
    }


    private void insereAnimacoes() {
        editText_email = (EditText) findViewById(R.id.editText_email);
        editText_password = (EditText) findViewById(R.id.editText_passwordRegisto);
        email_cima = (TextView) findViewById(R.id.textview_email);
        email_baixo = (TextView) findViewById(R.id.textview_under_email);
        password_cima = (TextView) findViewById(R.id.textview_password);
        password_baixo = (TextView) findViewById(R.id.textview_under_password);
        fromSide = AnimationUtils.loadAnimation(this, R.anim.from_side);
        editText_email.setAnimation(fromSide);
        editText_password.setAnimation(fromSide);
        email_cima.setAnimation(fromSide);
        email_baixo.setAnimation(fromSide);
        password_cima.setAnimation(fromSide);
        password_baixo.setAnimation(fromSide);
    }


    private boolean verificaEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    private boolean verificaVazio(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    private void desativaBotao() {
        botaoNext.setEnabled(false);
        botaoNext.setBackgroundResource(R.drawable.botao_next_desativado);
    }

    private void ativaBotao() {
        botaoNext.setEnabled(true);
        botaoNext.setBackgroundResource(R.drawable.botao_login);
    }

    private void verificaData() {

        if(!verificaVazio(editText_email) && verificaEmail(editText_email) && !verificaVazio(editText_password)
                && editText_password.getText().toString().length() >= 8)
        {
            ativaBotao();
        }else {
            ativaBotao();
        }

        if (!verificaEmail(editText_email)) {
            desativaBotao();
            editText_email.setError("Introduza um email válido");
        }

        if(editText_password.getText().toString().length() < 8)
        {
            desativaBotao();
            editText_password.setError("No minimo 8 caracteres");
        }
    }

    private void verificaMudancaEditText() {

        editText_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                verificaData();
            }
        });

        editText_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                verificaData();
            }
        });
    }
}
