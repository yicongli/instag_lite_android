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
import android.widget.Toast;

import com.unimelb.activity.EditProfileActivity;
import com.unimelb.activity.LoginActivity;
import com.unimelb.adapter.SquareImageAdapter;
import com.unimelb.constants.CommonConstants;
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

/**
 * Profile fragment
 */
public class ProfileFragment extends Fragment {
    private ProfileFragment profileFragment;

    private TextView postCountTv;
    private TextView followerCountTv;
    private TextView followingCountTv;
    private ImageView avatarIv;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        profileFragment = this;
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initView(view);
        initData();
        return view;
    }


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

        ExpandableGridView gridView = view.findViewById(R.id.profile_grid_view);
        final String[] imageUrls = new String[]{
                "http://pf3on5bei.sabkt.gdipper.com/1dbbf32d-8a1f-4194-a797-075dfcdbba38.jpeg",
                "http://pf3on5bei.sabkt.gdipper.com/1dbbf32d-8a1f-4194-a797-075dfcdbba38.jpeg",
                "http://pf3on5bei.sabkt.gdipper.com/24e3c68b-4042-4988-bfd9-694328a8b52c.jpeg",
        };

        SquareImageAdapter adapter = new SquareImageAdapter(this.getContext(), imageUrls);
        gridView.setAdapter(adapter);
    }

    public void initData() {
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
                JSONArray followers = (JSONArray) data.get("follows");
                JSONArray media = (JSONArray) data.get("media");
                JSONArray followings = (JSONArray) data.get("followed_by");
                String avatarUrl = data.get("profile_picture").toString();

                //update UI
                profileFragment.getActivity().runOnUiThread(() -> {
                    ImageUtils.loadRoundedImage(profileFragment.getActivity(), avatarUrl, avatarIv);
                    postCountTv.setText(String.valueOf(media.size()));
                    followerCountTv.setText(String.valueOf(followers.size()));
                    followingCountTv.setText(String.valueOf(followings.size()));
                });
            }
        });
    }
}
