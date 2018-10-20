package com.unimelb.entity;

/**
 * Basic User profile
 * This model is for display a user list in Search Tab
 */
public class BasicUserProfile {

    /**
     * user profile picture link
     */
    private String avatarUrl;

    /**
     * user email
     */
    private String bio;

    /**
     * user account name
     */
    private String username;

    /**
     * user id
     */
    private String userId;

    /**
     * Constructor function
     */
    public BasicUserProfile(String userId, String avatarUrl, String username, String bio) {
        this.userId = userId;
        this.avatarUrl = avatarUrl;
        this.bio = bio;
        this.username = username;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getBio() {
        return bio;
    }

    public String getUsername() {
        return username;
    }

    public String getUserId() {
        return userId;
    }
}
