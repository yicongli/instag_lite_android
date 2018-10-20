package com.unimelb.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.unimelb.activity.LoginActivity;
import com.unimelb.adapter.SquareImageAdapter;
import com.unimelb.constants.CommonConstants;
import com.unimelb.net.model.Medium;
import com.unimelb.instagramlite.R;
import com.unimelb.net.ErrorHandler;
import com.unimelb.net.HttpRequest;
import com.unimelb.net.IResponseHandler;
import com.unimelb.net.ResponseModel;
import com.unimelb.utils.ExpandableGridView;
import com.unimelb.utils.ImageUtils;
import com.unimelb.utils.TokenHelper;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Profile fragment
 */
public class ProfileFragment extends Fragment {
    /** Context */
    private ProfileFragment profileFragment;

    /** View component */
    private TextView postCountTv;
    private TextView followerCountTv;
    private TextView followingCountTv;
    private ImageView avatarIv;
    private ExpandableGridView gridView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        profileFragment = this;
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initView(view);
        initData();
        return view;
    }

    /**
     * Initialise view
     * @param view
     */
    public void initView(View view) {
        //logout button
        view.findViewById(R.id.logout_btn).setOnClickListener((imageView) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setMessage("Are you sure to logout?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        TokenHelper th = new TokenHelper(view.getContext());
                        th.deleteToken();
                        startActivity(new Intent(view.getContext(), LoginActivity.class));
                        profileFragment.getActivity().finish();
                    })
                    .setNegativeButton("No", null)
                    .create()
                    .show();
        });


        postCountTv = view.findViewById(R.id.profile_user_post_count);
        followerCountTv = view.findViewById(R.id.profile_user_follower_count);
        followingCountTv = view.findViewById(R.id.profile_user_following_count);
        avatarIv = view.findViewById(R.id.profile_user_avatar);
        gridView = view.findViewById(R.id.profile_grid_view);
    }

    /**
     * Fetch data and map data into views
     */
    public void initData() {
        // get user basic info
        HttpRequest.getInstance().doGetRequestAsync(CommonConstants.IP + "/api/v1/users/self", null, new IResponseHandler() {
            @Override
            public void onFailure(int statusCode, String errJson) {
                Activity context = profileFragment.getActivity();
                new ErrorHandler(context).handle(statusCode, errJson);
            }

            @Override
            public void onSuccess(String json) {
                System.out.println(json);
                ResponseModel rm = new ResponseModel(json);
                JSONObject data = rm.getData();
                JSONArray followers = (JSONArray) data.get("followed_by");
                JSONArray media = (JSONArray) data.get("media");
                JSONArray followings = (JSONArray) data.get("follows");
                String avatarUrl = data.get("profile_picture").toString();
                List<Medium> mediumList = new ArrayList<>();
                for (Object mediumObj : media) {
                    Medium medium = new Medium(mediumObj.toString());
                    mediumList.add(medium);
                }

                //update UI
                Activity context = profileFragment.getActivity();
                context.runOnUiThread(() -> {
                    ImageUtils.loadRoundedImage(context, avatarUrl, avatarIv);
                    postCountTv.setText(String.valueOf(media.size()));
                    followerCountTv.setText(String.valueOf(followers.size()));
                    followingCountTv.setText(String.valueOf(followings.size()));

                    List<String> imageUrls = new ArrayList<>();
                    for (Medium medium : mediumList) {
                        imageUrls.add(medium.getPhotoUrl());
                    }

                    SquareImageAdapter adapter = new SquareImageAdapter(context, imageUrls.toArray(new String[0]));
                    gridView.setAdapter(adapter);
                });
            }
        });
    }
}
