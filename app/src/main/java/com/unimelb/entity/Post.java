package com.unimelb.entity;

import com.unimelb.constants.CommonConstants;

import java.util.Date;

/**
 * Post Class for each posted picture
 */
public class Post {

    /**
     * post id
     */
    private String postId;

    /**
     * user profile picture link
     */
    private String avatarUrl;

    /**
     * user account name
     */
    private String username;

    /**
     * post image url
     */
    private String imageUrl;

    /**
     * post location
     */
    private String location;

    /**
     * post time and date string
     */
    private String dateString;

    /**
     * post date object
     */
    private Date date;

    /**
     * post likes
     */
    private int likesCount;

    /**
     * post comments
     */
    private int commentsCount;

    /**
     * latitude
     */
    private double lat;

    /**
     * longitude
     */
    private double lng;

    /**
     * Constructor
     *
     * @param postId
     * @param avatarUrl
     * @param username
     * @param imageUrl
     * @param location
     * @param dateString
     * @param date
     * @param likesCount
     * @param commentsCount
     * @param lat
     * @param lng
     */
    public Post(String postId, String avatarUrl, String username, String imageUrl, String location, String dateString, Date date, int likesCount, int commentsCount, double lat, double lng) {
        this.postId = postId;
        this.avatarUrl = avatarUrl;
        this.username = username;
        this.imageUrl = imageUrl;
        this.location = location;
        this.dateString = dateString;
        this.date = date;
        this.likesCount = likesCount;
        this.commentsCount = commentsCount;
        this.lat = lat;
        this.lng = lng;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
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

    public String getLocation() {
        return location;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public String getDateString() {
        return dateString;
    }

    public Date getDate() {
        return date;
    }

    /**
     * Calculate the distance of two points
     *
     * @param lat
     * @param lng
     * @return
     */
    public double getDistance(double lat, double lng) {
        return Math.sqrt((lat - this.lat) * (lat - this.lat) + (lng - this.lng) * (lng - this.lng));
    }

    /**
     * Calculate the distance of two points
     *
     * @return
     */
    public double getDistance() {
        return Math.sqrt((lat - CommonConstants.latitude) * (lat - CommonConstants.latitude) + (lng - CommonConstants.longitude) * (lng - CommonConstants.longitude));
    }
}
