package com.unimelb.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.scwang.smartrefresh.header.WaterDropHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.unimelb.activity.SearchActivity;
import com.unimelb.adapter.ImageAdapter;
import com.unimelb.instagramlite.R;
import com.unimelb.net.HttpRequest;
import com.unimelb.net.IResponseHandler;
import com.unimelb.utils.ImageUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

//        IResponseHandler responseHandler = new IResponseHandler() {
//            @Override
//            public void onFailure(int statusCode, String errMsg) {
//                Log.d("TAG", statusCode + "");
//                Log.d("TAG", errMsg);
//            }
//
//            @Override
//            public void onSuccess(String json) {
//                Log.d("TAG", json);
//            }
//        };
//        HttpRequest.getInstance().doGetRequestAsync("http://192.168.1.12:8080/api/v1/test", null, responseHandler);
    }

    public void initView(View view) {
        LinearLayout linearLayout = view.findViewById(R.id.search_bar);
        linearLayout.setOnClickListener((view1) -> startActivity(new Intent(this.getContext(), SearchActivity.class)));

        RefreshLayout refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new WaterDropHeader(this.getContext()));
        refreshLayout.setOnRefreshListener(layout -> {
            layout.finishRefresh(2000/*,false*/);// false means false
        });
        refreshLayout.setOnLoadMoreListener(layout -> {
            layout.finishLoadMore(2000/*,false*/);// false means false
        });

        GridView gridView = view.findViewById(R.id.search_grid_view);

        final String[] imageIds = new String[]{
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

        ImageAdapter adapter = new ImageAdapter(this.getContext(), imageIds);
        gridView.setAdapter(adapter);



//        TableLayout tableLayout = view.findViewById(R.id.search_table_layout);
//        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT,
//                1.0f
//        );
//        for (int i = 0; i < 5; i++) {
//            TableRow row = new TableRow(getActivity());
//            for (int j = 0; j < 3; j++) {
//                LinearLayout layout = new LinearLayout(getActivity());
//                layout.setLayoutParams(param);
//                ImageView imageView = new ImageView(getActivity());
//                layout.addView(imageView);
//                row.addView(layout);
//            }
//            tableLayout.addView(row);
//        }
    }


}
