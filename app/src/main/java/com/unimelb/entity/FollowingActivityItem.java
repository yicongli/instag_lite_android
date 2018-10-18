package com.unimelb.entity;

/**
 * Following activity list item
 */
public class FollowingActivityItem {

    /**
     * User avatar
     */
    private String avatarUrl;

    /**
     * Activity description
     */
    private String activityDesc;

    /**
     * Target photo
     */
    private String photoUrl;

    /**
     * Constructor function
     *
     * @param avatarUrl
     * @param activityDesc
     * @param photoUrl
     */
    public FollowingActivityItem(String avatarUrl, String activityDesc, String photoUrl) {
        this.avatarUrl = avatarUrl;
        this.activityDesc = activityDesc;
        this.photoUrl = photoUrl;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getActivityDesc() {
        return activityDesc;
    }

    public void setActivityDesc(String activityDesc) {
        this.activityDesc = activityDesc;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
