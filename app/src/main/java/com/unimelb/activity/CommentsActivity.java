package com.unimelb.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.unimelb.adapter.CommentListAdapter;
import com.unimelb.adapter.SearchListAdapter;
import com.unimelb.constants.CommonConstants;
import com.unimelb.entity.Comment;
import com.unimelb.instagramlite.R;
import com.unimelb.net.ErrorHandler;
import com.unimelb.net.HttpRequest;
import com.unimelb.net.IResponseHandler;
import com.unimelb.net.ResponseModel;
import com.unimelb.net.model.CommentMo;
import com.unimelb.net.model.User;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity shows the comments from other users for each post.
 */
public class CommentsActivity extends AppCompatActivity {
    /* context */
    private CommentsActivity context;
    /* listView */
    private RecyclerView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        context = this;
        initView();
        initData();
    }

    /**
     * Initialise views
     */
    private void initView() {
        findViewById(R.id.comment_back_btn).setOnClickListener((view) -> finish());
        listView = findViewById(R.id.comment_list);
        listView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * Fetch data and map data
     */
    private void initData() {
        Intent intent = getIntent();
        String postId = intent.getStringExtra("postId");
        System.out.println(postId);
        List<CommentMo> commentMoList = new ArrayList<>();

        HttpRequest.getInstance().doGetRequestAsync(CommonConstants.IP +
                "/api/v1/media/" + postId + "/comments", null, new IResponseHandler() {
            @Override
            public void onFailure(int statusCode, String errJson) {
                new ErrorHandler(context).handle(statusCode, errJson);
            }

            @Override
            public void onSuccess(String json) {
                ResponseModel rm = new ResponseModel(json);
                JSONObject data = rm.getData();
                JSONArray comments = (JSONArray) data.get("comments");
                for (int i = 0; i < comments.size(); i++) {
                    CommentMo commentMo = new CommentMo(comments.get(i).toString());
                    commentMoList.add(commentMo);
                }

                context.runOnUiThread(() -> {
                    List<Comment> commentList = new ArrayList<>();
                    for(CommentMo commentMo : commentMoList){
                        Comment comment = new Comment(commentMo.getContent(),
                                commentMo.getUser().getUsername(),
                                commentMo.getUser().getAvatarUrl(), commentMo.getCreatedAt());
                        commentList.add(comment);
                    }
                    listView.setAdapter(new CommentListAdapter(context, commentList));
                });
            }
        });
    }
}
