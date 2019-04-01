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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CreatePostActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    FirebaseAuth mFirebaseAuth;
    FirebaseStorage mFirebaseStorage;
    StorageReference mStorageReference;
    FirebaseDatabase mFirebaseDatabase;
    FirebaseUser mFirebaseUser;
    DatabaseReference mDatabaseReference;
    ImageButton mImageButton;
    EditText postTitle;
    EditText postDesc;
    Button submitPost;
    Uri uri;
    private static final int RC_PHOTO_PICKER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String mUsername = mFirebaseUser.getDisplayName();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("Post");

        mImageButton = findViewById(R.id.postImage);
        postTitle = findViewById(R.id.postTitle);
        postDesc = findViewById(R.id.postDesc);
        submitPost = findViewById(R.id.submitPost);

        Toolbar toolbar = findViewById(R.id.toolbarPost);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        mDrawerLayout = findViewById(R.id.drawer_layout_post);


        NavigationView navigationView = findViewById(R.id.nav_view_post);
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
                                Log.e("android:","Home clicked");
                                startActivity(new Intent(CreatePostActivity.this,MainActivity.class));
                                    break;
                            case R.id.nav_profile:
                                startActivity(new Intent(CreatePostActivity.this,ProfileActivity.class));
                                break;
                            case R.id.nav_signout:
                                mFirebaseAuth.getInstance().signOut();
                                break;
                        }

                        return true;
                    }
                });

            mFirebaseStorage = FirebaseStorage.getInstance();
            mStorageReference = mFirebaseStorage.getReference().child("blog_photos");

          mImageButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                  intent.setType("image/jpeg");
                  intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                  startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
              }
          });

          submitPost.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {

                  final String id = mFirebaseUser.getUid();

                  final String desc = postDesc.getText().toString().trim();
                  final String title = postTitle.getText().toString().trim();

                final  StorageReference photoRef = mStorageReference.child(uri.getLastPathSegment());
                  photoRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadUri = uri;
                             DatabaseReference reference = mDatabaseReference.child(id).push();
                            Post post = new Post(downloadUri.toString(),title,desc,false);
                             reference.setValue(post);
                            Toast.makeText(CreatePostActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(CreatePostActivity.this,MainActivity.class));
                        }
                    });
                }
            });

              }
          });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK){
             uri = data.getData();
            mImageButton.setImageURI(uri);
            //
        }
        super.onActivityResult(requestCode, resultCode, data);
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
