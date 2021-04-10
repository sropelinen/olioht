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

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

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
    private ImageView profilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar_main);
        drawerLayout = findViewById(R.id.drawer_layout_main);
        navigationView = findViewById(R.id.navigationView);
        profilePic = findViewById(R.id.profile_pic);

        Profile profile = Profile.getInstance();
        homeFragment = new HomeFragment(profile);
        settingsFragment = new SettingsFragment(profile);
        chartsFragment = new ChartsFragment(profile);
        profileEditFragment = new ProfileEditFragment(profile);

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
        AccountManager.getManager(this); //ToDo Testi, kirjautuu sisään automaattisesti(ei passaa unohtaa tätä kommenttia tähän)
    }

    private void setFragment() {
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
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
}