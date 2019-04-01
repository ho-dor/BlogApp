package com.example.kunal.socialapp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private EditText editText;
    private ListView searchList;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private String uid;
    private ArrayList<ProfileDetails> nameList;
    private SearchAdapter nameAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("Users").child(uid);

        nameList = new ArrayList<>();
        nameAdapter = new SearchAdapter(this,R.layout.search_list,nameList);
        listView = findViewById(R.id.searchList);
        listView.setAdapter(nameAdapter);

        editText = findViewById(R.id.searchView);
        searchList = findViewById(R.id.searchList);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                final String name = charSequence.toString();
                mDatabaseReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        ProfileDetails profileDetails = dataSnapshot.getValue(ProfileDetails.class);
                        if (profileDetails.getName().equals(name)) {
                            if (nameAdapter.getPosition(profileDetails)<0) {
                                nameAdapter.add(profileDetails);
                            }
                       }else if(!(profileDetails.getName().equals(name))){
                            if(nameAdapter.getPosition(profileDetails)>=0){
                                nameAdapter.remove(profileDetails);
                            }
                        }
// else if(!(profileDetails.getName().equals(name))){
//                            if(nameAdapter.getPosition(name)>=0){
//                                nameAdapter.remove(name);
//                            }
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
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
