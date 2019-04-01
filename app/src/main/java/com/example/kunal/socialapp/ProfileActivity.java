package com.example.kunal.socialapp;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    FirebaseAuth mFirebaseAuth;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference databaseReference;
    DrawerLayout mDrawerLayout;
    FirebaseUser mFirebaseUser;
    ChildEventListener mChildEventListener;
    Button editButton ;
    TextView name;
    TextView penName;
    TextView about;
    TextView occupation;
    CircleImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_activity);

        Toolbar toolbar = findViewById(R.id.toolbarProfile);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_profile);

        mDrawerLayout = findViewById(R.id.drawer_layout_profile);

        NavigationView navigationView = findViewById(R.id.nav_view_profile);
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
                                startActivity(new Intent(ProfileActivity.this,MainActivity.class));
                                break;
                            case R.id.nav_signout:
                                mFirebaseAuth.getInstance().signOut();
                                break;
                        }

                        return true;
                    }
                });
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = mFirebaseDatabase.getReference().child("Users").child(id);

        editButton = findViewById(R.id.editbutton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this,EditProfileActivity.class));
            }
        });

        String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();


        name = findViewById(R.id.name);
        penName = findViewById(R.id.penName);
        about = findViewById(R.id.about);
        occupation = findViewById(R.id.occupation);
        imageView = findViewById(R.id.profile_image);


        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ProfileDetails pd = dataSnapshot.getValue(ProfileDetails.class);
                name.setText(pd.getName());
                penName.setText(pd.getPenName());
                about.setText(pd.getAbout());
                occupation.setText(pd.getOccupation());
                Uri uri = Uri.parse(pd.getImage());
                imageView.setImageURI(uri);

                }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
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

    @Override
    protected void onStart() {
        databaseReference.addChildEventListener(mChildEventListener);
        super.onStart();

    }
}
