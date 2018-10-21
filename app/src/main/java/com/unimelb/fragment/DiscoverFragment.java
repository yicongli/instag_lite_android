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

/**
 * Discover fragment.
 */
public class DiscoverFragment extends Fragment {
    /* context */
    private DiscoverFragment context;

    /* suggestionList for search input */
    private List<BasicUserProfile> suggestionList = new ArrayList<>();

    /* list view */
    private RecyclerView listView;

    /* search adapter */
    private SearchListAdapter searchListAdapter;

    public DiscoverFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = this;
        View view = inflater.inflate(R.layout.fragment_discover, container, false);
        initView(view);

        return view;
    }

    private void initData(RefreshLayout refreshLayout) {
        HttpRequest.getInstance().doGetRequestAsync(CommonConstants.IP +
                        "/api/v1/search/suggest",null, new IResponseHandler() {
            @Override
            public void onFailure(int statusCode, String errJson) {
                new ErrorHandler(context.getActivity()).handle(statusCode, errJson);
            }

            @Override
            public void onSuccess(String json) {
                System.out.println(json);

                ResponseModel rm = new ResponseModel(json);
                JSONObject data = rm.getData();
                JSONArray users = (JSONArray) data.get("suggest_users");
                List<User> userList = new ArrayList<>();
                for (int i = 0; i < users.size(); i++) {
                    User user = new User(users.get(i).toString());
                    userList.add(user);
                }

                suggestionList.clear();
                context.getActivity().runOnUiThread(() -> {
                    for (User user : userList) {
                        BasicUserProfile profile = new BasicUserProfile(user.getId(),
                                user.getAvatarUrl(), user.getUsername(), user.getBio());
                        suggestionList.add(profile);
                    }
                    searchListAdapter.notifyDataSetChanged();
                    refreshLayout.finishRefresh();
                });
            }
        });
    }

    /*
    * Initialize Views
    * */
    public void initView(View view) {
        LinearLayout linearLayout = view.findViewById(R.id.search_bar);
        linearLayout.setOnClickListener((view1) -> startActivity(new Intent(this.getContext(),
                SearchActivity.class)));

        RefreshLayout refreshLayout = view.findViewById(R.id.refresh_layout);
        refreshLayout.autoRefresh();
        refreshLayout.setRefreshHeader(new WaterDropHeader(this.getContext()));
        refreshLayout.setOnRefreshListener(this::initData);
//        refreshLayout.setOnLoadMoreListener(layout -> {
//            layout.finishLoadMore(2000/*,false*/);// false means false
//        });

        listView = view.findViewById(R.id.search_suggestion_list);
        listView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        searchListAdapter = new SearchListAdapter(context.getActivity(), suggestionList);
        listView.setAdapter(searchListAdapter);
    }


}
