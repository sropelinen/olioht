package com.github.sropelinen.olioht;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Fragment fragment = null;
    private Fragment homeFragment = null;
    private Fragment settingsFragment = null;
    private Fragment chartsFragment = null;
    private Fragment profileEditFragment = null;
    private ActionBar actionBar;
    private Profile profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar_main);
        drawerLayout = findViewById(R.id.drawer_layout_main);
        navigationView = findViewById(R.id.navigationView);
        View header = navigationView.getHeaderView(0);
        profile = Profile.getInstance();

        homeFragment = new HomeFragment(profile);
        settingsFragment = new SettingsFragment(profile);
        chartsFragment = new ChartsFragment(profile);
        profileEditFragment = new ProfileEditFragment(profile);

        // set profile values to side menu
        TextView tvUsername = header.findViewById(R.id.tv_nav_username);
        TextView tvName = header.findViewById(R.id.tv_nav_name);
        tvUsername.setText(profile.getValue("userName").toString());
        tvName.setText(String.format("%s %s", profile.getValue("firstName").toString(),
                profile.getValue("lastName").toString()));

        //set home fragment on launch
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_main, homeFragment);
        transaction.commit();

        // set action bar
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                    R.string.open_drawer_text, R.string.close_drawer_text){};
            drawerLayout.addDrawerListener(toggle);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle("Home");
            toggle.syncState();
            setFragment();
        }
    }

    public void setProfileEditFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_main, profileEditFragment);
        transaction.commit();
        actionBar.setTitle("Edit profile");
    }

    public void setAddTravelFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_main, new AddTravelFragment(profile));
        transaction.commit();
        actionBar.setTitle("New travel");
    }

    public void setSettingsFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_main, settingsFragment);
        transaction.commit();
        actionBar.setTitle("Settings");
    }

    public void setHomeFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_main, homeFragment);
        transaction.commit();
        actionBar.setTitle("Home");
    }

    /* this method sets fragment based on choices in side drawer*/
    private void setFragment() {
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_logout) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
                return false;
            }
            if (id == R.id.menu_home) {
                fragment = homeFragment;
            } else if (id == R.id.menu_settings) {
                fragment = settingsFragment;
            } else if (id == R.id.menu_charts) {
                fragment = chartsFragment;
            }
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.frame_main, fragment);
            transaction.commit();
            drawerLayout.closeDrawers();
            actionBar.setTitle(item.getTitle());
            return false;
        });
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        toggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_main, homeFragment);
        transaction.commit();
        drawerLayout.closeDrawers();
        actionBar.setTitle("Home");
    }
}