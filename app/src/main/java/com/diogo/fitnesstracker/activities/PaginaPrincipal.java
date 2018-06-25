package com.diogo.fitnesstracker.activities;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.diogo.fitnesstracker.Fragments.DiarioFragment;
import com.diogo.fitnesstracker.Fragments.MetasFragment;
import com.diogo.fitnesstracker.Fragments.PerfilFragment;
import com.diogo.fitnesstracker.Fragments.ProgressoFragment;
import com.diogo.fitnesstracker.R;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class PaginaPrincipal extends AppCompatActivity {

    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teste_bottom_nav);

        toolbar = getSupportActionBar();
        toolbar.setTitle("Diário");

        configuraBottomNavView();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,new DiarioFragment()).commit();

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
        MenuItem menuItem = menu.getItem(2);
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
                        return true;
                    case R.id.navigation_metas:
                        fragmentTransaction.replace(R.id.frame_layout,new MetasFragment()).commit();
                        return true;
                    case R.id.navigation_diario:
                        fragmentTransaction.replace(R.id.frame_layout,new DiarioFragment()).commit();
                        return true;
                    case R.id.navigation_progresso:
                        fragmentTransaction.replace(R.id.frame_layout,new ProgressoFragment()).commit();
                        return true;
                }
                return false;
            }
        });
    }

}
