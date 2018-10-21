package com.unimelb.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.scwang.smartrefresh.header.WaterDropHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.unimelb.activity.DeviceListActivity;
import com.unimelb.adapter.PostImageAdapter;
import com.unimelb.constants.CommonConstants;
import com.unimelb.entity.Post;
import com.unimelb.instagramlite.R;
import com.unimelb.net.ErrorHandler;
import com.unimelb.net.HttpRequest;
import com.unimelb.net.IResponseHandler;
import com.unimelb.net.ResponseModel;
import com.unimelb.net.model.Medium;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class HomeFragment extends Fragment {
    /* context */
    private HomeFragment homeFragment;

    private RecyclerView postListView;

    private boolean sortDateOrder = true;

    private TextView emptyTipTv;

    private PostImageAdapter postListAdapter;

    /**
     * List of posts
     */
    private List<Post> postList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        homeFragment = this;
        initView(view);
        initData();
        return view;
    }

    /**
     * Initialise views
     *
     * @param view
     */
    public void initView(View view) {
        RefreshLayout refreshLayout = view.findViewById(R.id.refresh_layout);
        refreshLayout.setRefreshHeader(new WaterDropHeader(this.getContext()));
        refreshLayout.setOnRefreshListener(layout -> {
            layout.finishRefresh(2000);// false means false
        });
//        refreshLayout.setOnLoadMoreListener(layout -> {
//            layout.finishLoadMore(2000/*,false*/);// false means false
//        });

        postListView = view.findViewById(R.id.post_list);
        emptyTipTv = view.findViewById(R.id.post_empty_label);

        // Sort button onClick listener
        view.findViewById(R.id.home_sort_btn).setOnClickListener((btnView) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
            builder.setMessage("Select sort method?")
                    .setNegativeButton("by Date", (dialog, which) -> {
                        sortByDate();
                    })
                    .setPositiveButton("by Location", (dialog, which) -> {
                        sortByLocation();
                    })
                    .setNeutralButton("Cancel", null)
                    .create()
                    .show();
        });

        // Bluetooth button onClick listener
        view.findViewById(R.id.home_bluetooth_btn).setOnClickListener((bluetoothView) -> {
            Intent intent1 = new Intent(getContext(), DeviceListActivity.class);
            getContext().startActivity(intent1);
        });
    }

    /**
     * Initialize post data
     */
    private void initData() {
        HttpRequest.getInstance().doGetRequestAsync(CommonConstants.IP + "/api/v1/media/recent", null, new IResponseHandler() {
            @Override
            public void onFailure(int statusCode, String errJson) {
                new ErrorHandler(homeFragment.getActivity()).handle(statusCode, errJson);
            }

            @Override
            public void onSuccess(String json) {
                System.out.println(json);
                ResponseModel rm = new ResponseModel(json);
                JSONObject data = rm.getData();
                JSONArray media = (JSONArray) data.get("media");
                List<Medium> mediumList = new ArrayList<>();

                for (Object mediumObj : media) {
                    Medium medium = new Medium(mediumObj.toString());
                    mediumList.add(medium);
                }

                Activity context = homeFragment.getActivity();
                context.runOnUiThread(() -> {
                    for (Medium medium : mediumList) {
                        Post post = new Post(medium.getMediumId(), medium.getUser().getAvatarUrl(), medium.getUser().getUsername(), medium.getPhotoUrl(), medium.getLocation(), medium.getPostDateString(), medium.getPostDate(), medium.getLikes().size(), medium.getComments().size(), medium.getLat(), medium.getLng());
                        postList.add(post);
                    }
                    postListView.setLayoutManager(new LinearLayoutManager(context));
                    postListAdapter = new PostImageAdapter(context, postList);
                    postListView.setAdapter(postListAdapter);

                    if(postList.size() == 0){
                        emptyTipTv.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }


    /**
     * Sort by date
     */
    public void sortByDate() {
        if (sortDateOrder) {
            Collections.sort(postList, (post1, post2) -> post2.getDate().compareTo(post1.getDate()));
        } else {
            Collections.sort(postList, (post1, post2) -> post1.getDate().compareTo(post2.getDate()));
        }
        sortDateOrder = !sortDateOrder;
        postListAdapter.notifyDataSetChanged();
    }

    /**
     * Sort by location
     */
    public void sortByLocation() {
        Collections.sort(postList, (post1, post2) -> (int) (post1.getDistance() - post2.getDistance()));
        postListAdapter.notifyDataSetChanged();
    }
}