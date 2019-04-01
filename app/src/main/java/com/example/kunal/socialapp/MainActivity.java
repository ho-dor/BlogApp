package com.example.kunal.socialapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ui.ProgressDialogHolder;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {



    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;


    DatabaseReference mLikeReference;

    FirebaseAuth mFirebaseAuth;
    FirebaseUser mFirebaseUser;
    FirebaseAuth.AuthStateListener mAuthStateListener;
    FirebaseStorage mFirebaseStorage;
    StorageReference mStorageReference;
    private DrawerLayout mDrawerLayout;
    private String id;
    private static final int RC_SIGN_IN = 1;
    ListView listView;
    ChildEventListener value;
    static PostAdapter mAdapter;
    ImageView likeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

       likeButton = findViewById(R.id.like);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mStorageReference = mFirebaseStorage.getReference().child("profile_photos");
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //String mUsername = mFirebaseUser.getDisplayName();
        mLikeReference = mFirebaseDatabase.getReference().child("Post");



        //mFirebaseAuth.addAuthStateListener(mAuthStateListener);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,CreatePostActivity.class));
            }
        });

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(mFirebaseUser != null){
                    Log.i("User","Signed In");
                    id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Log.i("AuthStateChanged",""+id);
                    Toast.makeText(MainActivity.this, "You are Signed in as "+ mFirebaseUser.getDisplayName(), Toast.LENGTH_SHORT).show();
                    displayPosts();
                }else{
                    Log.i("User","Not Signed In");
                    // Choose authentication providers
                    List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build());

            // Create and launch sign-in intent
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .setLogo(R.drawable.logo)
                            .build(),
                    RC_SIGN_IN);
                }
            }
        };


        NavigationView navigationView = findViewById(R.id.nav_view);
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

                            case R.id.nav_profile:
                                startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                                break;
                            case R.id.nav_signout:
                                mFirebaseAuth.signOut();
                              //  ActivityCompat.finishAffinity(MainActivity.this);
                                 mFirebaseAuth.addAuthStateListener(mAuthStateListener);
                                break;
                            case R.id.nav_search:
                                startActivity(new Intent(MainActivity.this,SearchActivity.class));
                                break;
                            case R.id.upgrade:
                                startActivity(new Intent(MainActivity.this,UpgradeActivity.class));
                        }

                        return true;
                    }
                });

        listView = findViewById(R.id.listView);

//        progressBar = findViewById(R.id.indeterminate);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//                Log.i("Click ListItem","clicked listItem");
//                Post postItem = (Post)adapterView.getItemAtPosition(i);
//
//                Intent intent = new Intent(MainActivity.this,SinglePostActivity.class);
//                intent.putExtra("post",postItem);
//                startActivity(intent);
//
//            }
//        });

    }

    public void displayPosts(){
       final ArrayList<Post> mList = new ArrayList<>();
        listView = findViewById(R.id.listView);
        mAdapter = new PostAdapter(this,R.layout.post_list,mList);
        listView.setAdapter(mAdapter);
        Log.i("User id",""+id);
        mDatabaseReference = mFirebaseDatabase.getReference().child("Post").child(id);
        mDatabaseReference.keepSynced(true);
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.i("onChildAdded", "Method invoked");
                    //progressBar.setVisibility(View.VISIBLE);
                    Post post = dataSnapshot.getValue(Post.class);
                    String key = dataSnapshot.getKey();
                    mAdapter.add(post);
                    listView.setAdapter(mAdapter);
                    mDatabaseReference.keepSynced(true);

                //progressBar.setVisibility(View.INVISIBLE);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
                Log.i("resultcode"+resultCode, "resultcode"+resultCode) ;
            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                id = user.getUid();
                Log.i("User id Activity result",""+user.getUid());
            } else {
                Toast.makeText(this, "Sign In Cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onStart() {
        Log.i("Start","onStart invoked");
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
        //finishActivity(RC_SIGN_IN);


        //displayOnStart();
        super.onStart();
    }
}
