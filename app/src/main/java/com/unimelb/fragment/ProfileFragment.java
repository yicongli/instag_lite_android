package com.unimelb.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.unimelb.activity.EditProfileActivity;
import com.unimelb.activity.RegisterActivity;
import com.unimelb.adapter.SquareImageAdapter;
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
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void initView(View view){
        GridView gridView = view.findViewById(R.id.profile_grid_view);
        final String[] imageUrls = new String[]{
                "http://pf3on5bei.sabkt.gdipper.com/1dbbf32d-8a1f-4194-a797-075dfcdbba38.jpeg",
                "http://pf3on5bei.sabkt.gdipper.com/1dbbf32d-8a1f-4194-a797-075dfcdbba38.jpeg",
                "http://pf3on5bei.sabkt.gdipper.com/24e3c68b-4042-4988-bfd9-694328a8b52c.jpeg",
                "http://pf3on5bei.sabkt.gdipper.com/1dbbf32d-8a1f-4194-a797-075dfcdbba38.jpeg",
                "http://pf3on5bei.sabkt.gdipper.com/1dbbf32d-8a1f-4194-a797-075dfcdbba38.jpeg",
                "http://pf3on5bei.sabkt.gdipper.com/1dbbf32d-8a1f-4194-a797-075dfcdbba38.jpeg",
                "http://pf3on5bei.sabkt.gdipper.com/1dbbf32d-8a1f-4194-a797-075dfcdbba38.jpeg",
                "http://pf3on5bei.sabkt.gdipper.com/24e3c68b-4042-4988-bfd9-694328a8b52c.jpeg",
                "http://pf3on5bei.sabkt.gdipper.com/1dbbf32d-8a1f-4194-a797-075dfcdbba38.jpeg",
        };

        SquareImageAdapter adapter = new SquareImageAdapter(this.getContext(), imageUrls);
        gridView.setAdapter(adapter);
    }

    public void goToEditProfile(View view){
        Intent intent=new Intent(this.getContext(),EditProfileActivity.class);
        startActivity(intent);
    }
}
