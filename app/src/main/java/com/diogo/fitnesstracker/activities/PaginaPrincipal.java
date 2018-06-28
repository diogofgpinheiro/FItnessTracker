package com.diogo.fitnesstracker.activities;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.diogo.fitnesstracker.Fragments.DiarioFragment;
import com.diogo.fitnesstracker.Fragments.MetasFragment;
import com.diogo.fitnesstracker.Fragments.PerfilFragment;
import com.diogo.fitnesstracker.R;
import com.diogo.fitnesstracker.helper.Permissao;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class PaginaPrincipal extends AppCompatActivity {

    private ActionBar toolbar;
    private String[] permissoesNecessarias = new String[]
            {
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.INTERNET,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.CAMERA
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_principal);

        toolbar = getSupportActionBar();
        toolbar.setTitle("Diário");

        //validar permissoes
        Permissao.validarPermissoes(permissoesNecessarias,this,1);

        configuraBottomNavView();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, new DiarioFragment()).commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for(int permissaoResultado : grantResults)
        {
            if(permissaoResultado == PackageManager.PERMISSION_DENIED)
            {
                alertaPermissao();
            }
        }
    }

    private void alertaPermissao()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Para utilizar o app é necessário aceitar as permissões");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(1);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void configuraBottomNavView()
    {
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavigation);
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableItemShiftingMode(true);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(true);


        //Navegação entre fragmentos
        NavegaFragmentos(bottomNavigationViewEx);

        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
    }

    private void NavegaFragmentos (BottomNavigationViewEx viewEx)
    {
        viewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                switch (item.getItemId())
                {
                    case R.id.navigation_perfil:
                        fragmentTransaction.replace(R.id.frame_layout,new PerfilFragment()).commit();
                        toolbar.setTitle("Perfil");
                        return true;
                    case R.id.navigation_metas:
                        fragmentTransaction.replace(R.id.frame_layout,new MetasFragment()).commit();
                        toolbar.setTitle("Metas");
                        return true;
                    case R.id.navigation_diario:
                        fragmentTransaction.replace(R.id.frame_layout,new DiarioFragment()).commit();
                        toolbar.setTitle("Diário");
                        return true;
                }
                return false;
            }
        });
    }

}
