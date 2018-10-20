package com.unimelb.entity;

/*
* Comment class for each comment to show
*
* */
public class Comment {

    /**
     * post comment
     */
    private String comment;

    /**
     * user who writes the comment
     */
    private String username;
    /*
    * User head image url
    * */
    private String avatarUrl;
    /*
    * date time String
    * */
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
