package com.codepath.instagram;

/**
 * Created by kpirwani on 2/2/16.
 */
public class InstagramPost {
    private String username;
    private String caption;

    private InstagramPostContentType contentType;
    private String contentUrl;
    private String location;
    private long createdTime;
    private long likesCount;
    private InstagramComment[] comments;

    public enum InstagramPostContentType {
        DEFAULT,
        PHOTO,
        VIDEO
    }

    public InstagramPost(String username, String caption, String contentUrl, String location, long createdTime, long likesCount, InstagramComment[] comments, InstagramPostContentType contentType) {
        this.username = username;
        this.caption = caption;
        this.contentUrl = contentUrl;
        this.location = location;
        this.createdTime = createdTime;
        this.likesCount = likesCount;
        this.comments = comments;
        this.contentType = contentType;
    }

    public String getUsername() {
        return username;
    }

    public String getCaption() {
        return caption;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public String getLocation() {
        return location;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public long getLikesCount() {
        return likesCount;
    }

    public InstagramComment[] getComments() {
        return comments;
    }

    public InstagramPostContentType getContentType() {
        return contentType;
    }

    public static class InstagramComment {

        private String username;
        private String comment;

        public InstagramComment(String username, String comment) {
            this.username = username;
            this.comment = comment;
        }

        public String getUsername() {
            return username;
        }

        public String getComment() {
            return comment;
        }

    }
}

