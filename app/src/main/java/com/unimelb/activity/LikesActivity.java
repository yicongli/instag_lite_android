package com.unimelb.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.unimelb.adapter.SearchListAdapter;
import com.unimelb.constants.CommonConstants;
import com.unimelb.entity.BasicUserProfile;
import com.unimelb.instagramlite.R;
import com.unimelb.net.ErrorHandler;
import com.unimelb.net.HttpRequest;
import com.unimelb.net.IResponseHandler;
import com.unimelb.net.ResponseModel;
import com.unimelb.net.model.User;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
/*
* An activity to show a list of users who "like" this post.
* */
public class LikesActivity extends AppCompatActivity {
    /*context*/
    private LikesActivity context;
    /*listView*/
    private RecyclerView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likes);
        context = this;
        initView();
        initData();
    }

    /**
     * Initialise views
     */
    private void initView() {
        findViewById(R.id.like_back_btn).setOnClickListener((view) -> finish());
        listView = findViewById(R.id.like_list);
        listView.setLayoutManager(new LinearLayoutManager(this));
    }
    /*
    * Initialise data
    * */
    private void initData() {
        Intent intent = getIntent();
        String postId = intent.getStringExtra("postId");
//        System.out.println(postId);
        List<User> userList = new ArrayList<>();

        HttpRequest.getInstance().doGetRequestAsync(CommonConstants.IP + "/api/v1/media/" + postId + "/likes", null, new IResponseHandler() {
            @Override
            public void onFailure(int statusCode, String errJson) {
                new ErrorHandler(context).handle(statusCode, errJson);
            }

            @Override
            public void onSuccess(String json) {
                ResponseModel rm = new ResponseModel(json);
                JSONObject data = rm.getData();
                JSONArray likes = (JSONArray) data.get("likes");
                for (int i = 0; i < likes.size(); i++) {
                    User user = new User(likes.get(i).toString());
                    userList.add(user);
                }

                context.runOnUiThread(() -> {
                    List<BasicUserProfile> likeList = new ArrayList<>();
                    for (User user : userList) {
                        BasicUserProfile profile = new BasicUserProfile(user.getId(), user.getAvatarUrl(), user.getUsername(), user.getBio());
                        likeList.add(profile);
                    }
                    listView.setAdapter(new SearchListAdapter(context, likeList));
                });
            }
        });
    }
}
