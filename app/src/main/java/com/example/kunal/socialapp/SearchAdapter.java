package com.example.kunal.socialapp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchAdapter extends ArrayAdapter<ProfileDetails> {

    private ProfileDetails profileDetails;

    public SearchAdapter(Context context, int resource, List<ProfileDetails> objects) {
        super(context, resource, objects);
        Log.i("Constructor",""+context);
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.search_list, parent, false);
        }

        CircleImageView userSearchView = convertView.findViewById(R.id.userSearchImage);
        TextView userSearchName = convertView.findViewById(R.id.userNameSearch);

        profileDetails = getItem(position);

        userSearchView.setVisibility(View.VISIBLE);
        Glide.with(userSearchView.getContext())
                .load(profileDetails.getImage())
                .into(userSearchView);


        userSearchName.setText(profileDetails.getName());

        return convertView;
    }


}
