package com.unimelb.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.scwang.smartrefresh.header.WaterDropHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.unimelb.activity.SearchActivity;
import com.unimelb.adapter.SearchListAdapter;
import com.unimelb.adapter.SquareImageAdapter;
import com.unimelb.entity.BasicUserProfile;
import com.unimelb.instagramlite.R;
import com.unimelb.net.HttpRequest;
import com.unimelb.net.IResponseHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Discover fragment.
 */
public class DiscoverFragment extends Fragment {
    /* suggestionList for search input */
    private List<BasicUserProfile> suggestionList;

    public DiscoverFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);
        initData();
        initView(view);

        return view;
    }

    private void initData() {
        suggestionList = new ArrayList<>();
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
            BasicUserProfile profile = new BasicUserProfile("0", url, "Test", "test username");
            suggestionList.add(profile);
        }
    }

    /*
    * Initialize Views
    * */
    public void initView(View view) {
        LinearLayout linearLayout = view.findViewById(R.id.search_bar);
        linearLayout.setOnClickListener((view1) -> startActivity(new Intent(this.getContext(), SearchActivity.class)));

        RefreshLayout refreshLayout = view.findViewById(R.id.refresh_layout);
        refreshLayout.setRefreshHeader(new WaterDropHeader(this.getContext()));
        refreshLayout.setOnRefreshListener(layout -> {
            layout.finishRefresh(2000/*,false*/);// false means false
        });
        refreshLayout.setOnLoadMoreListener(layout -> {
            layout.finishLoadMore(2000/*,false*/);// false means false
        });

        RecyclerView listView = view.findViewById(R.id.search_suggestion_list);
        listView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        listView.setAdapter(new SearchListAdapter(this.getActivity(), suggestionList));
    }


}
