package com.diogo.fitnesstracker.helper;

import android.util.Base64;

public class CodificadorBase64 {

    public static String codificaBase64(String texto)
    {
        return Base64.encodeToString(texto.getBytes(),Base64.DEFAULT ).replaceAll("(\\n|\\r)","");
    }

    public static String descodificaBase64(String textCodificado)
    {
        return new String(Base64.decode(textCodificado,Base64.DEFAULT));
    }
}
