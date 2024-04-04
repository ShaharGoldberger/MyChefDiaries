package com.example.mychefdiaries.Model;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import com.example.mychefdiaries.R;
import com.example.mychefdiaries.fragments.FavoritesFragment;
import com.example.mychefdiaries.fragments.FeedFragment;
import com.example.mychefdiaries.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private ProfileFragment profileFragment;
    private FavoritesFragment favoritesFragment;
    private FeedFragment feedFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;
                if (menuItem.getItemId() == R.id.feed){
                    if (feedFragment == null) {
                        feedFragment = new FeedFragment();
                    }
                    fragment = feedFragment;
                } else if (menuItem.getItemId() == R.id.wish_lists){
                    if (favoritesFragment == null) {
                        favoritesFragment = new FavoritesFragment();
                    }
                    fragment = favoritesFragment;
                }else if (menuItem.getItemId() == R.id.profile){
                    if (profileFragment == null) {
                        profileFragment = new ProfileFragment();
                    }
                    fragment = profileFragment;
                }

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();
                return true;
            }
        });

        feedFragment = new FeedFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, feedFragment)
                .commit();


    }
}