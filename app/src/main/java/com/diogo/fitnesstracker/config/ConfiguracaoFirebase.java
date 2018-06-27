package com.diogo.fitnesstracker.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ConfiguracaoFirebase {

    private static FirebaseAuth autenticacao;
    private static DatabaseReference referenciaFirebase;
    private static StorageReference storage;

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

    public static StorageReference getFirebaseStorage()
    {
        if(storage == null)
        {
            storage = FirebaseStorage.getInstance().getReference();
        }
        return storage;
    }
}
