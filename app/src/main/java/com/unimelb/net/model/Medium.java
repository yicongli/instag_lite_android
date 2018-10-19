package com.unimelb.net.model;

import android.text.format.DateUtils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Map json to Medium object
 */
public class Medium {
    private String photoUrl;
    private List<String> tags = new ArrayList<>();
    private List<String> comments = new ArrayList<>();
    private List<String> likes = new ArrayList<>();
    private Date postDate;
    private User user;

    public Medium(String json) {
        JSONParser parser = new JSONParser();
        JSONObject obj;
        try {
            obj = (JSONObject) parser.parse(json);
            photoUrl = obj.get("image").toString();

            String createdAt = obj.get("createdAt").toString();
            System.out.println(createdAt);
            postDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(createdAt);

            JSONArray tagsArr = (JSONArray) obj.get("tags");
            if (tagsArr.size() > 0) {
                for (int i = 0; i < tagsArr.size(); i++) {
                    tags.add(tagsArr.get(i).toString());
                }
            }

            JSONArray commentArr = (JSONArray) obj.get("comments");
            if (commentArr.size() > 0) {
                for (int i = 0; i < commentArr.size(); i++) {
                    comments.add(commentArr.get(i).toString());
                }
            }

            JSONArray likesArr = (JSONArray) obj.get("likes");
            if (likesArr.size() > 0) {
                for (int i = 0; i < likesArr.size(); i++) {
                    likes.add(likesArr.get(i).toString());
                }
            }

            JSONObject userObj = (JSONObject) obj.get("user");
            if (userObj != null) {
                user = new User(userObj.toJSONString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getPhotoUrl() {
        return photoUrl;
    }


    public List<String> getTags() {
        return tags;
    }

    public List<String> getComments() {
        return comments;
    }


    public List<String> getLikes() {
        return likes;
    }

    public Date getPostDate() {
        return postDate;
    }

    public String getPostDateString() {
        return DateUtils.getRelativeTimeSpanString(postDate.getTime()).toString();
    }

    public User getUser() {
        return user;
    }

}
