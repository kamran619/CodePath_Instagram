package com.codepath.instagram;

import android.content.Context;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

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
        long creationTime = post.getCreatedTime() * 1000;
        String relativeTime = new PrettyTime().format(new Date(creationTime));
        tvCreationTime.setText(relativeTime);

        ImageView ivImage = (ImageView) convertView.findViewById(R.id.postImage);
        final VideoView mVideoView = (VideoView) convertView.findViewById(R.id.postVideo);
        if (post.getContentType() == InstagramPost.InstagramPostContentType.PHOTO) {
            ivImage.setVisibility(View.VISIBLE);
            mVideoView.setVisibility(View.GONE);
            ivImage.getLayoutParams().height = post.getImageHeight();
            Picasso.with(getContext()).load(post.getContentUrl()).placeholder(R.drawable.placeholder).into(ivImage);
        } else if (post.getContentType() == InstagramPost.InstagramPostContentType.VIDEO) {
            ivImage.setVisibility(View.GONE);
            mVideoView.setVisibility(View.VISIBLE);
            mVideoView.setVideoPath(post.getContentUrl());
            MediaController mediaController = new MediaController(getContext());
            mediaController.setAnchorView(mVideoView);
            mVideoView.setMediaController(mediaController);
            mVideoView.requestFocus();
            mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                // Close the progress bar and play the video
                public void onPrepared(MediaPlayer mp) {
                    mVideoView.start();
                }
            });
        }

        TextView tvCaption = (TextView) convertView.findViewById(R.id.caption);
        String caption = post.getCaption();
        if (caption == null || caption.length() == 0) {
            tvCaption.setVisibility(View.GONE);
        } else {
            SpannableStringBuilder spannableCaption = new SpannableStringBuilder();

            spannableCaption.append(caption);
            colorHashtagsInText(spannableCaption, caption, 0);
            colorTagsInText(spannableCaption, caption, 0);
            tvCaption.setText(spannableCaption);
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
        final int COMMENT_LIMIT = 2;
        for (int i = 0; i < COMMENT_LIMIT; i++) {
            String username = comments[i].getUsername();
            String comment = comments[i].getComment();
            commentsString.append(username);
            int spanBeginning = commentsString.length() - username.length();
            commentsString.setSpan(new StyleSpan(Typeface.BOLD), spanBeginning, username.length() + spanBeginning, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            commentsString.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.colorPrimary)), spanBeginning, username.length() + spanBeginning, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            commentsString.append(" ");
            commentsString.append(comment);
            int commentSpanBeginning = commentsString.length() - comment.length();
            colorHashtagsInText(commentsString, comment, commentSpanBeginning);
            colorTagsInText(commentsString, comment, commentSpanBeginning);
            if (i < comments.length - 1) {
                commentsString.append("\n");
            }
        }

        return commentsString;
    }

    private void colorHashtagsInText(SpannableStringBuilder inBuilder, String text, int spanBeginning) {
        final String hashtagDelimeter = "#";
        colorSymbolInText(hashtagDelimeter, inBuilder, text, spanBeginning);
    }

    private void colorTagsInText(SpannableStringBuilder inBuilder, String text, int spanBeginning) {
        final String tagDelimeter = "@";
        colorSymbolInText(tagDelimeter, inBuilder, text, spanBeginning);
    }

    private void colorSymbolInText(String symbol, SpannableStringBuilder inBuilder, String text, int spanBeginning) {
            boolean commentContainsSymbol = text.contains(symbol);
            if (commentContainsSymbol == true) {
                for (int index = text.indexOf(symbol); index >= 0; index = text.indexOf(symbol, index + 1)) {

                    int endOfSymbol = text.indexOf(" ", index + 1);
                    if (endOfSymbol == -1) {
                        endOfSymbol = text.length();
                    }
                    inBuilder.setSpan(new StyleSpan(Typeface.ITALIC), index + spanBeginning, endOfSymbol + spanBeginning, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    inBuilder.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.colorPrimary)), index + spanBeginning, endOfSymbol + spanBeginning, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
}
