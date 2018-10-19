package com.unimelb.net.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.util.List;

/**
 * Medium Entity
 */
public class Medium {
    private String photoUrl;
    private List<String> tags;
    private List<String> comments;
    private List<String> likes;

    public Medium(String json){
        JSONParser parser = new JSONParser();
        JSONObject obj;
        try {
            obj = (JSONObject) parser.parse(json);
            photoUrl = obj.get("image").toString();
            JSONArray tagsArr = (JSONArray) obj.get("tags");
            for(Object tag : tagsArr){
                tags.add(tag.toString());
            }
            JSONArray commentArr = (JSONArray) obj.get("comments");
            for(Object comment : commentArr){
                comments.add(comment.toString());
            }
            JSONArray likesArr = (JSONArray) obj.get("likes");
            for(Object like : likesArr){
                likes.add(like.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public List<String> getLikes() {
        return likes;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
    }
}
