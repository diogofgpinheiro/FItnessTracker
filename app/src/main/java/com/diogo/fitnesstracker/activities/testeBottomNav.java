package com.diogo.fitnesstracker.activities;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.diogo.fitnesstracker.Fragments.Diario;
import com.diogo.fitnesstracker.R;

public class testeBottomNav extends AppCompatActivity {

    private ActionBar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teste_bottom_nav);

        toolbar = getSupportActionBar();
        carregaFragmento(new Diario());


        toolbar.setTitle("Diario");
    }


    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragmento;
            switch (item.getItemId())
            {
                case R.id.navigation_perfil:
                    toolbar.setTitle("Perfil");
                    return true;
                case R.id.navigation_metas:
                    toolbar.setTitle("Metas");
                    return true;
                case R.id.navigation_diario:
                    toolbar.setTitle("Diario");
                    return true;
                case R.id.navigation_progresso:
                    toolbar.setTitle("Progresso");
                    return true;
            }
            return false;
        }
    };
    private void carregaFragmento(Fragment fragment) {
        // load fragment
        FragmentTransaction transacao = getSupportFragmentManager().beginTransaction();
        transacao.replace(R.id.frame_container, fragment);
        transacao.addToBackStack(null);
        transacao.commit();
    }
}
