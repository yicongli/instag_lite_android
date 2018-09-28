package com.unimelb.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;

import com.unimelb.instagramlite.R;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ImageButton backBtn = findViewById(R.id.search_back);
        backBtn.setOnClickListener((view) -> this.finish());
    }
}
