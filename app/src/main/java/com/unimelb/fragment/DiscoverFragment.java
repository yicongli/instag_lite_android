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
import android.widget.LinearLayout;

import com.scwang.smartrefresh.header.WaterDropHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.unimelb.activity.SearchActivity;
import com.unimelb.adapter.SquareImageAdapter;
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
        initView(view);

        return view;
    }

    public void initView(View view) {
        LinearLayout linearLayout = view.findViewById(R.id.search_bar);
        linearLayout.setOnClickListener((view1) -> startActivity(new Intent(this.getContext(), SearchActivity.class)));

        RefreshLayout refreshLayout = view.findViewById(R.id.refresh_layout);
        refreshLayout.setRefreshHeader(new WaterDropHeader(this.getContext()));
        refreshLayout.setOnRefreshListener(layout -> {
            layout.finishRefresh(2000/*,false*/);// false means false
        });
        refreshLayout.setOnLoadMoreListener(layout -> {
            layout.finishLoadMore(2000/*,false*/);// false means false
        });

        GridView gridView = view.findViewById(R.id.search_grid_view);

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


}
