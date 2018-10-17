package com.unimelb.entity;

public class Comment {

    /** post comment*/
    private String comment;

    /** user who writes the comment*/
    private String username;

    public Comment(String comment, String username) {
        this.comment = comment;
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
