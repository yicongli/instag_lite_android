package com.unimelb.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unimelb.instagramlite.R;
import com.unimelb.net.HttpRequest;
import com.unimelb.net.IResponseHandler;

/**
 * Discover fragment.
 */
public class DiscoverFragment extends Fragment {


    public DiscoverFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        IResponseHandler responseHandler = new IResponseHandler() {
            @Override
            public void onFailure(int statusCode, String errMsg) {
                Log.d("TAG", statusCode + "");
                Log.d("TAG", errMsg);
            }

            @Override
            public void onSuccess(String json) {
                Log.d("TAG", json);
            }
        };
        HttpRequest.getInstance().doGetRequestAsync("http://192.168.1.12:8080/api/v1/test", null, responseHandler);
    }
}
