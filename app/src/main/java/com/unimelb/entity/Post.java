package com.unimelb.entity;

import android.location.Location;

import java.util.List;

public class Post {

    /** post id*/
    private int postId;

    /** user profile picture link */
    private String avatarUrl;

    /** user account name */
    private String username;

    /** post image url */
    private String imageUrl;

    /** post location */
    private Location location;

    /** post time and date */
    private String date;

    /** post likes */
    private List<String> likes;

    /** post comments */
    private List<Comment> comments;

    public Post(int postId, String avatarUrl, String username, String imageUrl, Location location, String date, List<String> likes, List<Comment> comments) {
        this.postId = postId;
        this.avatarUrl = avatarUrl;
        this.username = username;
        this.imageUrl = imageUrl;
        this.location = location;
        this.date = date;
        this.likes = likes;
        this.comments = comments;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getLikes() {
        return likes;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
