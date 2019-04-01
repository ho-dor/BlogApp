package com.example.kunal.socialapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends AppCompatActivity {

    private Button commentButton;
    private TextView commentText;
    private EditText commentEdit;
    private DrawerLayout mDrawerLayout;
    private FirebaseAuth mFirebaseAuth;
    private TextView commentName;
    private DatabaseReference mCommentReference;
    private String userComment;
    private String userName;
    private DatabaseReference secondCommentReference;
    private int count=0;
    private int pos;
    private List<Comment> commentList;
    private CommentAdapter adapter;
    private ListView commentListView;
    private DatabaseReference thirdReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        commentEdit = findViewById(R.id.editComment);
        commentButton = findViewById(R.id.commentButton);
        commentText = findViewById(R.id.commentText);
        commentName = findViewById(R.id.commentName);

        mDrawerLayout = findViewById(R.id.drawer_layout_comment);
        mFirebaseAuth = FirebaseAuth.getInstance();
        final String uid = mFirebaseAuth.getCurrentUser().getUid();

        mCommentReference = FirebaseDatabase.getInstance().getReference().child("Post").child(uid);

        pos = getIntent().getIntExtra("postPosition",0);

        Toolbar toolbar = findViewById(R.id.toolbarSingleComment);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        NavigationView navigationView = findViewById(R.id.nav_view_comment);
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
                                startActivity(new Intent(CommentActivity.this,MainActivity.class));
                                break;
                            case R.id.nav_profile:
                                startActivity(new Intent(CommentActivity.this,ProfileActivity.class));
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



        commentList = new ArrayList<Comment>();
        commentListView = findViewById(R.id.commentList);
        adapter = new CommentAdapter(this,R.layout.comment_list,commentList);

commentListView.setAdapter(adapter);

        mCommentReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                count++;
                Log.i("onChildAdded", "Method invoked");
                //progressBar.setVisibility(View.VISIBLE);
                if(count-1==pos) {
                    Post post = dataSnapshot.getValue(Post.class);
                    String key = dataSnapshot.getKey();

                    secondCommentReference = mCommentReference.child(key).child("Comment").child(uid);
                    //thirdReference = mCommentReference.child(key).child("Comment");
                    secondCommentReference.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            Comment mComment = dataSnapshot.getValue(Comment.class);
                            adapter.add(mComment);
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




    commentButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            count=0;
            mCommentReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    count++;
                    Log.i("onChildAdded", "Method invoked");
                    //progressBar.setVisibility(View.VISIBLE);
                    if(count-1==pos) {
                        Post post = dataSnapshot.getValue(Post.class);
                        String key = dataSnapshot.getKey();

                        userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                        userComment = commentEdit.getText().toString().trim();
                        final Comment comment = new Comment(userName,userComment);
                        secondCommentReference.push().setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(CommentActivity.this, "Comment Added", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
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
