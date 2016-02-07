package com.codepath.instagram;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.instagram.com.codepath.instagram.picassoutils.CircleTransform;
import com.ocpsoft.pretty.time.PrettyTime;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;


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
        Picasso.with(getContext()).load(post.getProfilePictureLink()).transform(new CircleTransform()).into(ivAvatar);

        TextView tvLocation = (TextView) convertView.findViewById(R.id.location);
        tvLocation.setText(post.getLocation());

        TextView tvCreationTime = (TextView) convertView.findViewById(R.id.postTime);
        long creationTime = post.getCreatedTime();
        String relativeTime = new PrettyTime().format(new Date(creationTime));
        tvCreationTime.setText(relativeTime);

        ImageView ivImage = (ImageView) convertView.findViewById(R.id.postImage);
        if (post.getContentType() == InstagramPost.InstagramPostContentType.PHOTO) {
            Picasso.with(getContext()).load(post.getContentUrl()).into(ivImage);
        }

        TextView tvCaption = (TextView) convertView.findViewById(R.id.caption);
        String caption = post.getCaption();
        if (caption == null || caption.length() == 0) {
            tvCaption.setVisibility(View.GONE);
        } else {
            tvCaption.setText(post.getCaption());
        }

        TextView tvLikes = (TextView) convertView.findViewById(R.id.likes);
        String commaDelimitedLikes = String.format("%,d", post.getLikesCount());
        tvLikes.setText(commaDelimitedLikes + " likes");

        TextView tvComments = (TextView) convertView.findViewById(R.id.comments);
        tvComments.setText(getCommentsString(post));

        return convertView;
    }

    private SpannableStringBuilder getCommentsString(InstagramPost post) {
        SpannableStringBuilder commentsString = new SpannableStringBuilder();
        InstagramPost.InstagramComment comments[] = post.getComments();
        for (int i = 0; i < comments.length; i++) {
            String username = comments[i].getUsername();
            String comment = comments[i].getComment();
            commentsString.append(username);
            int spanBeginning = commentsString.length() - username.length();
            commentsString.setSpan(new StyleSpan(Typeface.BOLD), spanBeginning, username.length() + spanBeginning, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            commentsString.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.colorBahamaBlue)), spanBeginning, username.length() + spanBeginning, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            commentsString.append(" ");
            commentsString.append(comment);
            if (i < comments.length - 1) {
                commentsString.append("\n\n");
            }
        }

        return commentsString;
    }


}
