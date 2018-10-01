package com.unimelb.entity;

/**
 * Basic User profile
 * This model is for display a user list in Search Tab
 */
public class BasicUserProfile {

    /** user profile picture link */
    private String avatarUrl;

    /** user name */
    private String name;

    /** user account name */
    private String username;

    /**
     * Constructor function
     * @param avatarUrl
     * @param name
     * @param username
     */
    public BasicUserProfile(String avatarUrl, String name, String username){
        this.avatarUrl = avatarUrl;
        this.name = name;
        this.username = username;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
