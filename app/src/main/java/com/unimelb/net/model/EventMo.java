package com.unimelb.net.model;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
/*
* A class to convert jason string to Event Model
* */
public class EventMo {

    private String sourceUsername;

    private String sourceAvatar;

    private String targetPhoto;

    private String targetUsername;

    private String action;

    public EventMo(String json) {
        JSONParser parser = new JSONParser();
        try {
            JSONObject obj = (JSONObject) parser.parse(json);
            targetPhoto = obj.get("target_photo").toString();
            action = obj.get("action").toString();
            sourceUsername = obj.get("source_username").toString();
            sourceAvatar = obj.get("source_avatar").toString();
            targetUsername = obj.get("target_username").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getSourceUsername() {
        return sourceUsername;
    }

    public String getSourceAvatar() {
        return sourceAvatar;
    }

    public String getTargetPhoto() {
        return targetPhoto;
    }

    public String getTargetUsername() {
        return targetUsername;
    }

    public String getAction() {
        return action;
    }

    public String getEvent(){
        return sourceUsername + " " + action + " " + targetUsername + "'s post.";
    }
}
