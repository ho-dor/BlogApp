package com.example.kunal.socialapp;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

public class SinglePostActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private FirebaseAuth mFirebaseAuth;

    private ImageView imageView;
    private TextView title;
    private TextView desc;
    private ImageView likeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_post);
        mDrawerLayout = findViewById(R.id.drawer_layout_single);
        mFirebaseAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbarSingle);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        Post postItem = (Post)getIntent().getSerializableExtra("postItem");

        likeButton = findViewById(R.id.postedLike);
        imageView = findViewById(R.id.postedImageSingle);
        title = findViewById(R.id.postedTitleSingle);
        desc = findViewById(R.id.postedDescSingle);


        imageView.setVisibility(View.VISIBLE);
        Glide.with(imageView.getContext())
                .load(postItem.getImage())
                .into(imageView);

        title.setText(postItem.getTitle());
        desc.setText(postItem.getDesc());
        likeButton.setImageResource(R.drawable.ic_thumb_up);

        NavigationView navigationView = findViewById(R.id.nav_view_single);
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
                                startActivity(new Intent(SinglePostActivity.this,MainActivity.class));
                                break;
                            case R.id.nav_profile:
                                startActivity(new Intent(SinglePostActivity.this,ProfileActivity.class));
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
