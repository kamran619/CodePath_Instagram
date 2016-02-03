package com.codepath.instagram;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kpirwani on 2/2/16.
 */
public class UserPostAdapter extends ArrayAdapter<InstagramPost> {
    public UserPostAdapter(Context context, ArrayList<InstagramPost> posts) {
        super(context, 0 ,posts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_post, parent, false);
        }

        InstagramPost post = getItem(position);

        TextView tvUsername = (TextView) convertView.findViewById(R.id.username);
        tvUsername.setText(post.getUsername());

        ImageView ivAvatar = (ImageView) convertView.findViewById(R.id.avatar);
        //TODO: set image with picaso

        TextView tvLocation = (TextView) convertView.findViewById(R.id.location);
        tvLocation.setText(post.getLocation());

        TextView tvCreationTime = (TextView) convertView.findViewById(R.id.postTime);
        //TODO: set relative time
        tvCreationTime.setText(String.valueOf(post.getCreatedTime()));

        ImageView ivImage = (ImageView) convertView.findViewById(R.id.postImage);
        //TODO: set image with picasso

        TextView tvLikes = (TextView) convertView.findViewById(R.id.likes);
        tvLikes.setText(String.valueOf(post.getLikesCount()));

        TextView tvComments = (TextView) convertView.findViewById(R.id.comments);
        //TODO: set comments by parsing

        return convertView;
    }


}
