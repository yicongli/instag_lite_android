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
import com.unimelb.constants.CommonConstants;
import com.unimelb.entity.BasicUserProfile;
import com.unimelb.entity.FollowingActivityItem;
import com.unimelb.instagramlite.R;
import com.unimelb.net.ErrorHandler;
import com.unimelb.net.HttpRequest;
import com.unimelb.net.IResponseHandler;
import com.unimelb.net.ResponseModel;
import com.unimelb.net.model.EventMo;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity Tab Fragment.
 */
public class ActivityFragment extends Fragment {
    private ActivityFragment context;
    private RecyclerView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity, container, false);
        context = this;
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        listView = view.findViewById(R.id.following_activity_list);
        listView.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }

    private void initData() {
        HttpRequest.getInstance().doGetRequestAsync(CommonConstants.IP + "/api/v1/event/self", null, new IResponseHandler() {
            @Override
            public void onFailure(int statusCode, String errJson) {
                new ErrorHandler(context.getActivity()).handle(statusCode, errJson);
            }

            @Override
            public void onSuccess(String json) {
                System.out.println(json);
                List<EventMo> eventMoList = new ArrayList<>();
                ResponseModel rm = new ResponseModel(json);
                JSONObject data = rm.getData();
                JSONArray events = (JSONArray) data.get("events");
                for (int i = 0; i < events.size(); i++) {
                    EventMo eventMo = new EventMo(events.get(i).toString());
                    eventMoList.add(eventMo);
                }

                List<FollowingActivityItem> activities = new ArrayList<>();
                context.getActivity().runOnUiThread(() -> {
                    List<BasicUserProfile> likeList = new ArrayList<>();
                    for (EventMo eventMo : eventMoList) {
                        FollowingActivityItem activity = new FollowingActivityItem(eventMo.getSourceAvatar(), eventMo.getEvent(), eventMo.getTargetPhoto());
                        activities.add(activity);
                    }
                    listView.setAdapter(new FollowingActivityListAdapter(context.getActivity(), activities));
                });
            }
        });
    }
}
