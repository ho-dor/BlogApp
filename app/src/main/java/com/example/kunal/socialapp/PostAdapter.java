package com.example.kunal.socialapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class PostAdapter extends ArrayAdapter<Post> {
    public PostAdapter(Context context, int resource, List<Post> objects) {
        super(context, resource, objects);
        this.context = context;
        Log.i("Constructor",""+context);
    }

    private Context context;
    private DatabaseReference likeReference;
    private FirebaseUser user;
    private DatabaseReference secondReference;
    private int pos;
    private Post post;
    private int count = 0;
    private String key;
    private DatabaseReference shareReference;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.post_list, parent, false);
        }

        ImageView postedImage =  convertView.findViewById(R.id.postedImage);
        TextView postedDesc =  convertView.findViewById(R.id.postedDesc);
        TextView postedTitle =  convertView.findViewById(R.id.postedTitle);
        final ImageView like = convertView.findViewById(R.id.like);
        final ImageView comment = convertView.findViewById(R.id.comment);
        final ImageView share = convertView.findViewById(R.id.share);

       post = getItem(position);


//        boolean isPhoto = post.getImage() != null;
//        boolean isTitle = post.getTitle() != null;
//        boolean isDesc = post.getDesc() != null;
//        if (!isDesc && !isTitle) {
//            postedDesc.setVisibility(View.GONE);
//            postedTitle.setVisibility(View.GONE);
//            if(isPhoto) {
//                postedImage.setVisibility(View.VISIBLE);
//                Glide.with(postedImage.getContext())
//                        .load(post.getImage())
//                        .into(postedImage);
//                }
//            }
//        else if(!isDesc && !isPhoto) {
//
//            postedDesc.setVisibility(View.GONE);
//            postedImage.setVisibility(View.GONE);
//
//            if(isTitle){
//
//            postedTitle.setVisibility(View.VISIBLE);
//            postedTitle.setText(post.getTitle());
//            }
//        }else {
//
//            postedDesc.setText(post.getDesc());
//        }
        postedImage.setVisibility(View.VISIBLE);
                Glide.with(postedImage.getContext())
                        .load(post.getImage())
                        .into(postedImage);

        postedTitle.setText(post.getTitle());
        postedDesc.setText(post.getDesc());
        if(post.isLiked()) {
            like.setImageResource(R.drawable.like);
        }else{
            like.setImageResource(R.drawable.ic_thumb_up);
        }
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();



        postedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,SinglePostActivity.class);
                intent.putExtra("postItem",post);
                context.startActivity(intent);
            }
        });

        postedDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,SinglePostActivity.class);
                intent.putExtra("postItem",post);
                context.startActivity(intent);
            }
        });

        postedTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,SinglePostActivity.class);
                intent.putExtra("postItem",post);
                context.startActivity(intent);
            }
        });





        likeReference = FirebaseDatabase.getInstance().getReference().child("Post").child(uid);
        likeReference.keepSynced(true);


        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count=0;
                RelativeLayout relativeLayout = (RelativeLayout) like.getParent();
                View v =(CardView) relativeLayout.getParent();
                ListView listView = (ListView) v.getParent();
                pos = listView.getPositionForView(view);
                //post = getItem(pos);
                Log.i("posti", "onClick: "+pos);

                Log.i("Like:","Like clicked");

                likeReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        count++;
                        Log.i("onChildAdded", "Method invoked");
                        //progressBar.setVisibility(View.VISIBLE);
                        if(count-1 == pos) {
                            Post post = dataSnapshot.getValue(Post.class);
                             key = dataSnapshot.getKey();
                            secondReference = likeReference.child(key);
                            if(post.isLiked()){
                                post.setLiked(false);
                                secondReference.child("liked").setValue(post.isLiked());
                                like.setImageResource(R.drawable.ic_thumb_up);
                            }else{
                                post.setLiked(true);
                                secondReference.child("liked").setValue(post.isLiked());
                                like.setImageResource(R.drawable.like);
                            }
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

        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RelativeLayout relativeLayout = (RelativeLayout) like.getParent();
                View v =(CardView) relativeLayout.getParent();
                ListView listView = (ListView) v.getParent();
                pos = listView.getPositionForView(view);

                Intent intent = new Intent(context,CommentActivity.class);
                intent.putExtra("postPosition",pos);
                context.startActivity(intent);
            }
        });

        shareReference = likeReference.child("Shared");


        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RelativeLayout relativeLayout = (RelativeLayout) like.getParent();
                View v =(CardView) relativeLayout.getParent();
                ListView listView = (ListView) v.getParent();
                pos = listView.getPositionForView(view);
                count=0;
                likeReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        count++;
                        Log.i("onChildAdded", "Method invoked");
                        //progressBar.setVisibility(View.VISIBLE);
                        if(count-1 == pos) {
                            Post postShared = dataSnapshot.getValue(Post.class);
                            key = dataSnapshot.getKey();
                            //secondReference = likeReference.child(key);
                            Intent intent = new Intent(getContext(),SharePostActivity.class);
                            intent.putExtra("postPos",pos);
                            getContext().startActivity(intent);

                        }
//                            if(post.isLiked()){
//                                post.setLiked(false);
//                                secondReference.child("liked").setValue(post.isLiked());
//                                like.setImageResource(R.drawable.ic_thumb_up);
//                            }else{
//                                post.setLiked(true);
//                                secondReference.child("liked").setValue(post.isLiked());
//                                like.setImageResource(R.drawable.like);
//                            }
                        }

                        //progressBar.setVisibility(View.INVISIBLE);

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


        return convertView;
    }

    @Override
    public int getCount() {

        return super.getCount();
    }
}

