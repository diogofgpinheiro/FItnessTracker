package com.diogo.fitnesstracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

public class Registar extends AppCompatActivity {

    private EditText editText_email;
    private EditText editText_password;
    private TextView email_cima,email_baixo,password_cima,password_baixo;
    private Animation fromSide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registar);

        // Insere animaçoes
        insereAnimacoes();
        //TODO verificar se os dados estao corretos
        //TODO passar para a proxima pagina
    }


    void insereAnimacoes()
    {
        editText_email = (EditText) findViewById(R.id.editText_email);
        editText_password = (EditText) findViewById(R.id.editText_passwordRegisto);
        email_cima = (TextView) findViewById(R.id.textview_email);
        email_baixo = (TextView) findViewById(R.id.textview_under_email);
        password_cima = (TextView) findViewById(R.id.textview_password);
        password_baixo = (TextView) findViewById(R.id.textview_under_password);
        fromSide = AnimationUtils.loadAnimation(this,R.anim.from_side);
        editText_email.setAnimation(fromSide);
        editText_password.setAnimation(fromSide);
        email_cima.setAnimation(fromSide);
        email_baixo.setAnimation(fromSide);
        password_cima.setAnimation(fromSide);
        password_baixo.setAnimation(fromSide);
    }


    boolean verificaEmail(EditText text)
    {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    boolean verificaVazio (EditText text)
    {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    void verificaData()
    {
        if(verificaVazio(editText_email))
        {
            editText_email.setError("Email vazio");
        }
    }
}
