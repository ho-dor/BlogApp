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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    FirebaseDatabase mFirebaseUserReference;
    DatabaseReference mDatabaseReferenceUser;
    DrawerLayout mDrawerLayout;
    FirebaseAuth mFirebaseAuth;
    StorageReference mStorageReferenceUser;
    FirebaseStorage mFirebaseStorageUser;
    //FirebaseDatabase mFirebaseDatabase;
    //DatabaseReference databaseReference;
   // FirebaseStorage mFirebaseStorage;
    //StorageReference mStorageReference;
    EditText editName;
    EditText editPenName;
    EditText editAbout;
    EditText editOccupation;
    Button saveEditButton;
    CircleImageView editImage;
    FirebaseUser mFirebaseUser;
    List<ProfileDetails> profileDetails;
    Uri uri;

    private static final int RC_PHOTO_PICKER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        editName = findViewById(R.id.editName);
        editImage = findViewById(R.id.profile_image_edit);
        editAbout = findViewById(R.id.about);
        editOccupation = findViewById(R.id.occupation);
        editPenName = findViewById(R.id.editPenName);
        saveEditButton = findViewById(R.id.editSaveButton);

        mFirebaseStorageUser = FirebaseStorage.getInstance();
        mStorageReferenceUser = mFirebaseStorageUser.getReference().child("user_images");

        mFirebaseUserReference = FirebaseDatabase.getInstance();
        mDatabaseReferenceUser = mFirebaseUserReference.getReference().child("Users");

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String username = mFirebaseUser.getDisplayName();

        //databaseReference = FirebaseDatabase.getInstance().getReference().child(username);

        profileDetails = new ArrayList<>();



        Toolbar toolbar = findViewById(R.id.toolbarEdit);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_edit);

        mDrawerLayout = findViewById(R.id.drawer_layout_edit);

        NavigationView navigationView = findViewById(R.id.nav_view_edit);
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
                                startActivity(new Intent(EditProfileActivity.this,MainActivity.class));
                                break;
                            case R.id.nav_signout:
                                mFirebaseAuth.getInstance().signOut();
                                break;
                        }

                        return true;
                    }
                });

        editImage = findViewById(R.id.profile_image_edit);
        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });

        mFirebaseAuth = FirebaseAuth.getInstance();

        //mFirebaseStorage = FirebaseStorage.getInstance();
        //mStorageReference = mFirebaseStorage.getReference().child(mFirebaseAuth.getCurrentUser().getDisplayName());

        saveEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String id = mFirebaseUser.getUid();
               final String name = editName.getText().toString().trim();
               final String penName = editPenName.getText().toString().trim();
               final String about = editAbout.getText().toString().trim();
               final String occupation = editOccupation.getText().toString().trim();

                final StorageReference photoRef = mStorageReferenceUser.child(uri.getLastPathSegment());
                photoRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                Uri downloadUri = uri;
                                DatabaseReference reference = mDatabaseReferenceUser.child(id);
                                ProfileDetails profile = new ProfileDetails(downloadUri.toString(),name,penName,about,occupation);
                                reference.setValue(profile);
                                Toast.makeText(EditProfileActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(EditProfileActivity.this,ProfileActivity.class));
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
             editImage.setImageURI(uri);

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
