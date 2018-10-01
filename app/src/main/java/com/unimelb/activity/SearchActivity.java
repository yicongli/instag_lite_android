package com.unimelb.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageButton;

import com.unimelb.adapter.SearchListAdapter;
import com.unimelb.entity.BasicUserProfile;
import com.unimelb.instagramlite.R;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private List<BasicUserProfile> searchResultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initData();
        initView();
    }

    private void initData() {
        searchResultList = new ArrayList<>();
        String[] imageUrls = new String[]{
                "http://pf3on5bei.sabkt.gdipper.com/profile18.jpg",
                "http://pf3on5bei.sabkt.gdipper.com/profile20.jpg",
                "http://pf3on5bei.sabkt.gdipper.com/profile25.jpg",
                "http://pf3on5bei.sabkt.gdipper.com/profile17.jpg",
                "http://pf3on5bei.sabkt.gdipper.com/profile30.jpg",
                "http://pf3on5bei.sabkt.gdipper.com/profile31.jpg",
                "http://pf3on5bei.sabkt.gdipper.com/profile28.jpg",
                "http://pf3on5bei.sabkt.gdipper.com/profile27.jpg",
                "http://pf3on5bei.sabkt.gdipper.com/profile26.jpg",
                "http://pf3on5bei.sabkt.gdipper.com/profile14.jpg",
                "http://pf3on5bei.sabkt.gdipper.com/profile15.jpg",
                "http://pf3on5bei.sabkt.gdipper.com/profile13.jpg",
                "http://pf3on5bei.sabkt.gdipper.com/profile9.jpg",
                "http://pf3on5bei.sabkt.gdipper.com/profile2.jpg",
                "http://pf3on5bei.sabkt.gdipper.com/profile10.jpg",
        };
        for (String url : imageUrls) {
            BasicUserProfile profile = new BasicUserProfile(url, "Test", "test username");
            searchResultList.add(profile);
        }
    }

    private void initView() {
        ImageButton backBtn = findViewById(R.id.search_back);
        backBtn.setOnClickListener((view) -> this.finish());
        EditText editText = findViewById(R.id.search_edit_text);
        RecyclerView listView = findViewById(R.id.search_list);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setAdapter(new SearchListAdapter(this, searchResultList));
    }

}
