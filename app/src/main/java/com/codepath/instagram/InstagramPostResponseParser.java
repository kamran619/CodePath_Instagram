package com.codepath.instagram;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kpirwani on 2/2/16.
 */
public class InstagramPostResponseParser {
    final static private String DATA_KEY = "data";
    final static private String TYPE_KEY = "type";
    final static private String IMAGE_KEY = "image";
    final static private String IMAGES_KEY = "images";
    final static private String HEIGHT_KEY = "height";

    final static private String STANDARD_RESOLUTION_KEY = "standard_resolution";
    final static private String URL_KEY = "url";

    final static private String VIDEO_KEY = "video";
    final static private String VIDEOS_KEY = "videos";

    final static private String LIKES_KEY = "likes";

    final static private String COUNT_KEY = "count";

    final static private String CAPTION_KEY = "caption";
    final static private String TEXT_KEY = "text";

    final static private String USER_KEY = "user";
    final static private String USERNAME_KEY = "username";
    final static private String PROFILE_PICTURE = "profile_picture";

    final static private String LOCATION_KEY = "location";
    final static private String NAME_KEY = "name";

    final static private String CREATED_TIME_KEY = "created_time";

    final static private String COMMENTS_KEY = "comments";

    final static private String FROM_KEY = "from";

    final static private int COMMENT_COUNT = 2;

    public static ArrayList<InstagramPost> parseResponse(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        ArrayList<InstagramPost> posts = new ArrayList<InstagramPost>();
        JSONArray jsonArray = jsonObject.getJSONArray(DATA_KEY);
        for (int i = 0; i < jsonArray.length(); i++) {
            jsonObject = jsonArray.getJSONObject(i);
            String type = jsonObject.getString(TYPE_KEY);

            String username = jsonObject.getJSONObject(USER_KEY).getString(USERNAME_KEY);
            String profilePictureLink = jsonObject.getJSONObject(USER_KEY).getString(PROFILE_PICTURE);

            InstagramPost.InstagramPostContentType contentType = InstagramPost.InstagramPostContentType.DEFAULT;
            String contentUrl = null;
            int imageHeight = 0;
            if (type.equalsIgnoreCase(IMAGE_KEY)) {
                contentType = InstagramPost.InstagramPostContentType.PHOTO;
                contentUrl = jsonObject.getJSONObject(IMAGES_KEY).getJSONObject(STANDARD_RESOLUTION_KEY).getString(URL_KEY);
                imageHeight = jsonObject.getJSONObject(IMAGES_KEY).getJSONObject(STANDARD_RESOLUTION_KEY).getInt(HEIGHT_KEY);
            } else if (type.equalsIgnoreCase(VIDEO_KEY)) {
                contentType = InstagramPost.InstagramPostContentType.VIDEO;
                contentUrl = jsonObject.getJSONObject(VIDEOS_KEY).getJSONObject(STANDARD_RESOLUTION_KEY).getString(URL_KEY);
            }

            int likesCount = jsonObject.getJSONObject(LIKES_KEY).getInt(COUNT_KEY);
            String caption = null;

            if (jsonObject.has(CAPTION_KEY)) {
                JSONObject captionJSONObject = jsonObject.optJSONObject(CAPTION_KEY);
                if (captionJSONObject != null) {
                    caption = captionJSONObject.getString(TEXT_KEY);
                }
            }

            String postLocation = null;
            if (jsonObject.has(LOCATION_KEY)) {
                JSONObject locationJSONObject = jsonObject.optJSONObject(LOCATION_KEY);
                if (locationJSONObject != null) {
                    postLocation = locationJSONObject.getString(NAME_KEY);
                }
            }

            long createdTime = Long.valueOf(jsonObject.getString(CREATED_TIME_KEY));

            InstagramPost.InstagramComment[] comments = new InstagramPost.InstagramComment[0];
            JSONArray commentsJSONArray = jsonObject.getJSONObject(COMMENTS_KEY).getJSONArray(DATA_KEY);
            if (commentsJSONArray.length() > 0) {
                int arrayLength = commentsJSONArray.length();
                comments = new InstagramPost.InstagramComment[arrayLength];
                for (int j = 0; j < commentsJSONArray.length(); j++) {
                    JSONObject commentJSONObject = commentsJSONArray.getJSONObject(j);
                    String commentText = commentJSONObject.getString(TEXT_KEY);
                    if (commentText == null) {
                        commentText = "";
                    }
                    String commentUsername = commentJSONObject.getJSONObject(FROM_KEY).getString(USERNAME_KEY);
                    if (commentUsername == null) {
                        commentUsername = "";
                    }
                    String commentPictureLink = commentJSONObject.getJSONObject(FROM_KEY).getString(PROFILE_PICTURE);
                    if (commentPictureLink == null) {
                        commentPictureLink = "";
                    }
                    InstagramPost.InstagramComment comment = new InstagramPost.InstagramComment(commentUsername, commentText, commentPictureLink);
                    comments[j] = comment;
                }
            }

            InstagramPost post = new InstagramPost(username, caption, contentUrl, postLocation, createdTime, likesCount, comments, contentType, profilePictureLink, imageHeight);
            posts.add(post);
        }

        return posts;
    }
}
