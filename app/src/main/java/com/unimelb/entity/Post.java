package com.unimelb.entity;

public class Post {

    /** user profile picture link */
    private String avatarUrl;

    /** user account name */
    private String username;

    /** post image url */
    private String imageUrl;

    /** post location */
    private String location;

    /** post time and date */
    private String date;

    /** post comments */
    private String comments;

    public Post(String avatarUrl, String username, String imageUrl, String location, String date, String comments) {
        this.avatarUrl = avatarUrl;
        this.username = username;
        this.imageUrl = imageUrl;
        this.location = location;
        this.date = date;
        this.comments = comments;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
