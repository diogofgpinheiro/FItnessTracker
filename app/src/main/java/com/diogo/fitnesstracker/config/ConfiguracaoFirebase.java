package com.diogo.fitnesstracker.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfiguracaoFirebase {

    private static FirebaseAuth autenticacao;
    private static DatabaseReference referenciaFirebase;

    //retorna a instancia do FirebaseDatabase
    public static DatabaseReference getReferenciaFirebase()
    {
        if(referenciaFirebase == null)
        {
            referenciaFirebase = FirebaseDatabase.getInstance().getReference();
        }
        return referenciaFirebase;
    }


    public static FirebaseAuth getAutenticacao()
    {
        if(autenticacao == null)
        {
            autenticacao = FirebaseAuth.getInstance();
        }
        return autenticacao;
    }
}
