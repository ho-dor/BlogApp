package com.example.kunal.socialapp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class CommentAdapter extends ArrayAdapter<Comment> {

    private TextView userName;
    private TextView userComment;
    private Comment comment;


    public CommentAdapter(Context context, int resource, List<Comment> objects) {
        super(context, resource, objects);
        Log.i("Constructor",""+context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.comment_list, parent, false);
        }

        userName = convertView.findViewById(R.id.commentName);
        userComment = convertView.findViewById(R.id.commentText);

        comment = getItem(position);

        userName.setText(comment.getUserName());
        userComment.setText(comment.getUserComment());

return convertView;
    }

}
