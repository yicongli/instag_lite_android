package com.unimelb.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.unimelb.activity.SearchActivity;
import com.unimelb.instagramlite.R;
import com.unimelb.net.HttpRequest;
import com.unimelb.net.IResponseHandler;
import com.unimelb.utils.ImageUtils;

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
        initView(view);

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

    public void initView(View view) {
        LinearLayout linearLayout = view.findViewById(R.id.search_bar);
        linearLayout.setOnClickListener((view1) -> startActivity(new Intent(this.getContext(), SearchActivity.class)));

        TableLayout tableLayout = view.findViewById(R.id.search_table_layout);
        for (int i = 0; i < 5; i++) {
            TableRow row = new TableRow(getActivity());
            ImageView imageView = new ImageView(getActivity());
            ImageUtils.loadImage(getContext(), "http://pf3on5bei.sabkt.gdipper.com/1dbbf32d-8a1f-4194-a797-075dfcdbba38.jpeg", imageView);
            imageView.setMaxWidth(30);
            imageView.setMaxHeight(30);
            row.addView(imageView);
            tableLayout.addView(row);
        }
    }
}
