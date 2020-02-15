package com.nynstd.moviesadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setfragment(new moviesfragment());
        BottomNavigationView bottomNavigationMenu =findViewById(R.id.btmnavmenu);
        bottomNavigationMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.moviesmenu){
                    setfragment(new moviesfragment());
                }
                if(menuItem.getItemId() == R.id.seriesmenu){
                    setfragment(new seriesfragment());
                }
                if(menuItem.getItemId() == R.id.catagoriesmenu){
                    setfragment(new categoriesfragment());
                }
                return true;
            }
        });
    }
    public void setfragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.mainfrag,fragment);
        ft.commit();
    }
}
