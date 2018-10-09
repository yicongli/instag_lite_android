package com.unimelb.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unimelb.adapter.PostImageAdapter;
import com.unimelb.entity.Post;
import com.unimelb.instagramlite.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class HomeFragment extends Fragment {

    private List<Post> postList;

    public HomeFragment (){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initData();
        initView(view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


//        HttpRequest.getInstance().doGetRequestAsync("http://192.168.1.12:8080/api/v1/test", null, new IResponseHandler(){
//            @Override
//            public void onFailure(int statusCode, String errMsg) {
//                Log.d("TAG", statusCode + "");
//                Log.d("TAG", errMsg);
//            }
//
//            @Override
//            public void onSuccess(String json) {
//                Log.d("TAG", json);
//            }
//        });
    }

    private void initData() {
        postList = new ArrayList<>();

        Calendar date = Calendar.getInstance();

        final String[] postImageUrls = new String[]{
                "http://pf3on5bei.sabkt.gdipper.com/1dbbf32d-8a1f-4194-a797-075dfcdbba38.jpeg",
                "http://pf3on5bei.sabkt.gdipper.com/1dbbf32d-8a1f-4194-a797-075dfcdbba38.jpeg",
                "http://pf3on5bei.sabkt.gdipper.com/24e3c68b-4042-4988-bfd9-694328a8b52c.jpeg",
                "http://pf3on5bei.sabkt.gdipper.com/1dbbf32d-8a1f-4194-a797-075dfcdbba38.jpeg",
                "http://pf3on5bei.sabkt.gdipper.com/1dbbf32d-8a1f-4194-a797-075dfcdbba38.jpeg",
                "http://pf3on5bei.sabkt.gdipper.com/1dbbf32d-8a1f-4194-a797-075dfcdbba38.jpeg",
                "http://pf3on5bei.sabkt.gdipper.com/1dbbf32d-8a1f-4194-a797-075dfcdbba38.jpeg",
                "http://pf3on5bei.sabkt.gdipper.com/24e3c68b-4042-4988-bfd9-694328a8b52c.jpeg",
                "http://pf3on5bei.sabkt.gdipper.com/1dbbf32d-8a1f-4194-a797-075dfcdbba38.jpeg",
                "http://pf3on5bei.sabkt.gdipper.com/1dbbf32d-8a1f-4194-a797-075dfcdbba38.jpeg",
                "http://pf3on5bei.sabkt.gdipper.com/1dbbf32d-8a1f-4194-a797-075dfcdbba38.jpeg",
                "http://pf3on5bei.sabkt.gdipper.com/1dbbf32d-8a1f-4194-a797-075dfcdbba38.jpeg",
                "http://pf3on5bei.sabkt.gdipper.com/1dbbf32d-8a1f-4194-a797-075dfcdbba38.jpeg",
                "http://pf3on5bei.sabkt.gdipper.com/24e3c68b-4042-4988-bfd9-694328a8b52c.jpeg",
                "http://pf3on5bei.sabkt.gdipper.com/1dbbf32d-8a1f-4194-a797-075dfcdbba38.jpeg",
        };

        for (String postUrl : postImageUrls) {
            Post post = new Post("http://pf3on5bei.sabkt.gdipper.com/profile18.jpg", "Test", postUrl, "location", date.getTime().toString(), "comments");
            postList.add(post);
        }
    }

    public void initView(View view) {
        RecyclerView listView = view.findViewById(R.id.post_list);
        listView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        listView.setAdapter(new PostImageAdapter(this.getContext(), postList));
    }
}
