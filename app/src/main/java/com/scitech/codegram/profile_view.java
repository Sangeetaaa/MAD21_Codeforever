package com.scitech.codegram;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikhaellopez.circularimageview.CircularImageView;

public class profile_view extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    CircularImageView headerImage;
    TextView headerUsername, headerEmail;
    NavigationView navigationView;
    DrawerLayout drawer;
    int[] menus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        menus = new int[]{R.id.nav_home, R.id.nav_performance, R.id.nav_codeAssistant
                , R.id.nav_contest, R.id.nav_forum,
                R.id.nav_logout};

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        headerImage = header.findViewById(R.id.nav_profilePic);
        headerUsername = (TextView) header.findViewById(R.id.nav_username);
        headerEmail = (TextView) header.findViewById(R.id.nav_email);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        headerUsername.setText(user.getDisplayName());
        headerEmail.setText(user.getEmail());
        Uri uri = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();

        Glide.with(getApplicationContext()).load(uri.toString()).into(headerImage);

        setTitle("Home");
        dashboard dash = new dashboard();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fragment, dash, dash.getTag()).commit();
        navigationView.setCheckedItem(R.id.nav_home);

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (checkNavigationMenuItem() != R.id.nav_home)
            {
                dashboard dash = new dashboard();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.fragment, dash, dash.getTag()).commit();
                setTitle("Home");
                drawer.closeDrawer(GravityCompat.START);
                navigationView.setCheckedItem(R.id.nav_home);
            }
            else
                super.onBackPressed();
        }
    }

    private int checkNavigationMenuItem() {
        Menu menu = navigationView.getMenu();
        for (int value : menus) {
            if (menu.findItem(value).isChecked())
                return value;
        }
        return -1;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == R.id.action_add){
            setTitle("New Blog");
            AddBlog addBlog = new AddBlog();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.fragment, addBlog, addBlog.getTag()).commit();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home)
        {
            setTitle("Home");
            dashboard dash = new dashboard();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.fragment, dash, dash.getTag()).commit();
        }
        else if (id == R.id.nav_performance)
        {
            setTitle("Performance Analyzer");
            PerformanceAnalyzer analyzer = new PerformanceAnalyzer();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.fragment, analyzer, analyzer.getTag()).commit();
        }

        else if (id == R.id.nav_forum)
        {
            setTitle("TechForum");
            TechForum techForum = new TechForum();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.fragment, techForum, techForum.getTag()).commit();
        }

        else if(id == R.id.nav_contest)
        {
            setTitle("Upcoming Contests");
            UpcomingContests contests = new UpcomingContests();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.fragment, contests, contests.getTag()).commit();
        }
        else if(id == R.id.nav_codeAssistant)
        {
            setTitle("Code-Assistant");
            CodeAssistant codeAssistant = new CodeAssistant();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.fragment, codeAssistant, codeAssistant.getTag()).commit();
        }
        else if(id == R.id.nav_logout)
        {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(this, "Successfully logged out", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(profile_view.this, login_page.class));
        }
        navigationView.setCheckedItem(id);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
