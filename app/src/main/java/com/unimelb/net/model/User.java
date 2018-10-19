package com.unimelb.net.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.List;

/**
 * Map json to User object
 */
public class User {

    private String avatarUrl;

    private String username;

//    private List<String> media;
//
//    private List<String> followers;
//
//    private List<String> followings;

    public User(String json) {
        JSONParser parser = new JSONParser();
        try {
            JSONObject obj = (JSONObject) parser.parse(json);
            avatarUrl = obj.get("profile_picture").toString();
            username = obj.get("email").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
}
