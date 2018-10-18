package com.unimelb.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unimelb.adapter.FollowingActivityListAdapter;
import com.unimelb.adapter.SearchListAdapter;
import com.unimelb.entity.FollowingActivityItem;
import com.unimelb.instagramlite.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity Tab Fragment.
 */
public class ActivityFragment extends Fragment {
    private List<FollowingActivityItem> activityList;

    /**
     * Constructor
     */
    public ActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity, container, false);
        initData();
        initView(view);
        return view;
    }

    public void initData() {
        activityList = new ArrayList<>();
        String[] avatarUrls = new String[]{
                "http://pgr1ie9ou.sabkt.gdipper.com/profile19.jpg",
                "http://pgr1ie9ou.sabkt.gdipper.com/profile19.jpg",
                "http://pgr1ie9ou.sabkt.gdipper.com/profile25.jpg",
        };

        String[] photoUrls = new String[]{
                "http://pgr1ie9ou.sabkt.gdipper.com/cd4fe26a-4ac5-499b-97dc-fcaf73c12235.jpeg",
                "http://pgr1ie9ou.sabkt.gdipper.com/cd4fe26a-4ac5-499b-97dc-fcaf73c12235.jpeg",
                "http://pgr1ie9ou.sabkt.gdipper.com/cd4fe26a-4ac5-499b-97dc-fcaf73c12235.jpeg",
        };

        for (int i = 0; i < avatarUrls.length; i++) {
            FollowingActivityItem profile = new FollowingActivityItem(avatarUrls[i], "jinge liked nasibsarvari's post.", photoUrls[i]);
            activityList.add(profile);
        }
    }

    public void initView(View view) {
        RecyclerView listView = view.findViewById(R.id.following_activity_list);
        listView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        listView.setAdapter(new FollowingActivityListAdapter(this.getContext(), activityList));
    }

}
