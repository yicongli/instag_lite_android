package com.unimelb.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unimelb.instagramlite.R;

/**
 * Activity Tab Fragment.
 */
public class ActivityFragment extends Fragment {


    /**
     * Constructor
     */
    public ActivityFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity, container, false);
        initView(view);
        return view;
    }

    public void initView(View view) {

    }

}
