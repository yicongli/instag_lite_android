package com.unimelb.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unimelb.activity.EditProfileActivity;
import com.unimelb.activity.RegisterActivity;
import com.unimelb.instagramlite.R;

/**
 * Profile fragment
 */
public class ProfileFragment extends Fragment {
    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void goToEditProfile(View view){
        Intent intent=new Intent(this.getContext(),EditProfileActivity.class);
        startActivity(intent);
    }
}
