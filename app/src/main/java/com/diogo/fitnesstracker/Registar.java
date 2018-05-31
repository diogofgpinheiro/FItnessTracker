package com.diogo.fitnesstracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;

public class Registar extends AppCompatActivity {

    private EditText email;
    private EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registar);

        //TODO verificar se os dados estao corretos
        email = (EditText) findViewById(R.id.editText_email);
        password = (EditText) findViewById(R.id.editText_passwordRegisto);

        verificaData();
        //TODO passar para a proxima pagina
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
        if(verificaVazio(email))
        {
            email.setError("Email vazio");
        }
    }
}
