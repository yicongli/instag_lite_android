package com.unimelb.net.model;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Map json to User object
 */
public class User {

    private String avatarUrl;

    private String username;

    private String email;

    private String bio;

    private String id;

//    private List<String> media;
//
//    private List<String> followers;
//
//    private List<String> followings;

    public User(String json) {
        JSONParser parser = new JSONParser();
        try {
            JSONObject obj = (JSONObject) parser.parse(json);
            id = obj.get("_id").toString();
            avatarUrl = obj.get("profile_picture").toString();
            email = obj.get("email").toString();
            username = obj.get("username").toString();
            bio = obj.get("bio").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getBio() {
        return bio;
    }

    public String getId() {
        return id;
    }
}
