package com.unimelb.entity;

public class Comment {

    /**
     * post comment
     */
    private String comment;

    /**
     * user who writes the comment
     */
    private String username;

    private String avatarUrl;

    private String date;

    public Comment(String comment, String username, String avatarUrl, String date) {
        this.comment = comment;
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.date = date;
    }

    public String getComment() {
        return username + ": " + comment;
    }

    public String getUsername() {
        return username;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getDate() {
        return date;
    }
}
