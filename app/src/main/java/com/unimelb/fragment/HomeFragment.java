package com.unimelb.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.scwang.smartrefresh.header.WaterDropHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.unimelb.adapter.PostImageAdapter;
import com.unimelb.entity.Comment;
import com.unimelb.entity.Post;
import com.unimelb.instagramlite.R;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;


public class HomeFragment extends Fragment {

    /**
     * List of posts
     */
    public List<Post> postList;

    /**
     * Date of the posts
     */
    Long date;

    /**
     * Post id
     */
    int postId;

    /**
     * Get last known location
     */
    private FusedLocationProviderClient mFusedLocationClient;

    Location myLocation;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy hh:mm a", Locale.US);

    Switch sortSwitch;
    Toast switchToast;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(view.getContext());
        lastKnownLocation(view);

        initData();

        // Sort by date initially
        sortByDate();

        initView(view);


        sortSwitchControl(view);

        return view;
    }

    /**
     * Initialize post data
     */
    private void initData() {
        postList = new ArrayList<>();
        date = System.currentTimeMillis();
        postId = 0;


        final String[] postImageUrls = new String[]{
                "http://pgr1ie9ou.sabkt.gdipper.com/cd4fe26a-4ac5-499b-97dc-fcaf73c12235.jpeg",
                "http://pgr1ie9ou.sabkt.gdipper.com/cd4fe26a-4ac5-499b-97dc-fcaf73c12235.jpeg",
                "http://pgr1ie9ou.sabkt.gdipper.com/cd4fe26a-4ac5-499b-97dc-fcaf73c12235.jpeg",
                "http://pgr1ie9ou.sabkt.gdipper.com/cd4fe26a-4ac5-499b-97dc-fcaf73c12235.jpeg",
                "http://pgr1ie9ou.sabkt.gdipper.com/cd4fe26a-4ac5-499b-97dc-fcaf73c12235.jpeg",
                "http://pgr1ie9ou.sabkt.gdipper.com/cd4fe26a-4ac5-499b-97dc-fcaf73c12235.jpeg",
                "http://pgr1ie9ou.sabkt.gdipper.com/cd4fe26a-4ac5-499b-97dc-fcaf73c12235.jpeg",
                "http://pgr1ie9ou.sabkt.gdipper.com/cd4fe26a-4ac5-499b-97dc-fcaf73c12235.jpeg",
                "http://pgr1ie9ou.sabkt.gdipper.com/cd4fe26a-4ac5-499b-97dc-fcaf73c12235.jpeg",
                "http://pgr1ie9ou.sabkt.gdipper.com/cd4fe26a-4ac5-499b-97dc-fcaf73c12235.jpeg",
        };

        List<String> testLikes = new ArrayList<>();
        testLikes.add("username1");
        testLikes.add("username2");

        List<Comment> testComments = new ArrayList<>();
        Comment comment = new Comment("Comment1", "username");
        testComments.add(comment);


        for (String postUrl : postImageUrls) {
            Post post = new Post(postId, "http://pf3on5bei.sabkt.gdipper.com/profile18.jpg", "Test", postUrl, myLocation, sdf.format(date), testLikes, testComments);
            postList.add(post);


            postId += 1;
            date -= 60000;
        }

    }

    public void initView(View view) {
        RefreshLayout refreshLayout = view.findViewById(R.id.refresh_layout);
        refreshLayout.setRefreshHeader(new WaterDropHeader(this.getContext()));
        refreshLayout.setOnRefreshListener(layout -> {
            layout.finishRefresh(2000/*,false*/);// false means false
        });
        refreshLayout.setOnLoadMoreListener(layout -> {
            layout.finishLoadMore(2000/*,false*/);// false means false
        });

        RecyclerView listView = view.findViewById(R.id.post_list);
        listView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        listView.setAdapter(new PostImageAdapter(this.getContext(), postList));
    }

    /**
     * Sort date/location switch
     *
     * @param view
     */
    public void sortSwitchControl(View view) {
        sortSwitch = view.findViewById(R.id.sortPostsSwitch);

        sortSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (sortSwitch.isChecked()) {
                    sortByLocation();
                    initView(view);
                    switchToast = Toast.makeText(view.getContext(), sortSwitch.getTextOn(), Toast.LENGTH_SHORT);
                    switchToast.show();
                } else {
                    sortByDate();
                    initView(view);
                    switchToast = Toast.makeText(view.getContext(), sortSwitch.getTextOff(), Toast.LENGTH_SHORT);
                    switchToast.show();

                }
            }
        });
    }

    /**
     * Sort by date
     */
    public void sortByDate() {
        Collections.sort(postList, new Comparator<Post>() {
            @Override
            public int compare(Post post1, Post post2) {
                return post2.getDate().compareTo(post1.getDate());
            }
        });

    }

    /**
     * Sort by location
     */
    public void sortByLocation() {
        //https://stackoverflow.com/questions/6927556/sorting-a-list-of-locations

       /* Collections.sort(postList, new Comparator<Post>() {
            @Override
            public int compare(Post post1, Post post2) {
                //System.out.println("horse: " + (post1.getLocation().distanceTo(myLocation) - post2.getLocation().distanceTo(myLocation)));
                return (int) (post1.getLocation().distanceTo(myLocation) - post2.getLocation().distanceTo(myLocation));
            }
        });*/

    }

    /**
     * Get last known location
     *
     * @param view
     */
    public void lastKnownLocation(View view) {

        myLocation = new Location("My location");

        if (ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener((Activity) view.getContext(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            myLocation = location;
                            //System.out.println("My Location: " + myLocation);
                            initData();
                            initView(view);
                        }
                    }
                });
    }

}
