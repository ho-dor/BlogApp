package com.example.kunal.socialapp;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

public class SharePostActivity extends AppCompatActivity {


    private DrawerLayout mDrawerLayout;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_post);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDrawerLayout = findViewById(R.id.drawer_layout_share);

        Toolbar toolbar = findViewById(R.id.toolbarSingleShare);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        NavigationView navigationView = findViewById(R.id.nav_view_share);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here
                        switch(menuItem.getItemId()){

                            case R.id.nav_home:
                                startActivity(new Intent(SharePostActivity.this,MainActivity.class));
                                break;
                            case R.id.nav_profile:
                                startActivity(new Intent(SharePostActivity.this,ProfileActivity.class));
                                break;
                            case R.id.nav_signout:
                                mFirebaseAuth.signOut();
                                //  ActivityCompat.finishAffinity(MainActivity.this);
                                // mFirebaseAuth.addAuthStateListener(mAuthStateListener);
                                break;
                        }

                        return true;
                    }
                });

int position = getIntent().getIntExtra("postPos",0);



    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
