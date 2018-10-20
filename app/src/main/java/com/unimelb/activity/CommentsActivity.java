package com.unimelb.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.unimelb.instagramlite.R;

public class CommentsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        initView();
    }

    /**
     * Initialise views
     */
    public void initView() {
        findViewById(R.id.comment_back_btn).setOnClickListener((view) -> finish());
    }
}
