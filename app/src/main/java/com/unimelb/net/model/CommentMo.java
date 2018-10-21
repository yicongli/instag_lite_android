package com.unimelb.net.model;

import android.text.format.DateUtils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
/*
* a Class to convert jason string to comment model
* */
public class CommentMo {
    private String content;

    private User user;

    private Date postDate;

    public CommentMo(String json) {
        JSONParser parser = new JSONParser();
        try {
            JSONObject obj = (JSONObject) parser.parse(json);
            content = obj.get("content").toString();
            String createdAt = obj.get("createdAt").toString();
            postDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                    Locale.getDefault()).parse(createdAt);

            JSONObject userObj = (JSONObject) obj.get("user");
            if (userObj != null) {
                user = new User(userObj.toJSONString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getContent() {
        return content;
    }

    public User getUser() {
        return user;
    }

    public String getCreatedAt() {
        return DateUtils.getRelativeTimeSpanString(postDate.getTime()).toString();
    }
}
