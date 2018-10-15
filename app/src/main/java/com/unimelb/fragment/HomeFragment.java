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
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.unimelb.adapter.PostImageAdapter;
import com.unimelb.entity.Post;
import com.unimelb.instagramlite.R;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class HomeFragment extends Fragment {

    private List<Post> postList;
    Long date;

    private FusedLocationProviderClient mFusedLocationClient;

    Location myLocation;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy hh:mm a");

    Switch sortSwitch;
    Toast switchToast;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(view.getContext());

        lastKnownLocation(view);

        initData();

        // Sort by date initally
        sortByDate();

        initView(view);


        sortSwitchControl(view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


//        HttpRequest.getInstance().doGetRequestAsync("http://192.168.1.12:8080/api/v1/test", null, new IResponseHandler(){
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
//        });
    }

    private void initData() {
        postList = new ArrayList<>();
        date = System.currentTimeMillis();


        final String[] postImageUrls = new String[]{
                "http://pf3on5bei.sabkt.gdipper.com/1dbbf32d-8a1f-4194-a797-075dfcdbba38.jpeg",
                "http://pf3on5bei.sabkt.gdipper.com/1dbbf32d-8a1f-4194-a797-075dfcdbba38.jpeg",
                "http://pf3on5bei.sabkt.gdipper.com/24e3c68b-4042-4988-bfd9-694328a8b52c.jpeg",
                "http://pf3on5bei.sabkt.gdipper.com/1dbbf32d-8a1f-4194-a797-075dfcdbba38.jpeg",
                "http://pf3on5bei.sabkt.gdipper.com/1dbbf32d-8a1f-4194-a797-075dfcdbba38.jpeg",
                "http://pf3on5bei.sabkt.gdipper.com/1dbbf32d-8a1f-4194-a797-075dfcdbba38.jpeg",
                "http://pf3on5bei.sabkt.gdipper.com/1dbbf32d-8a1f-4194-a797-075dfcdbba38.jpeg",
                "http://pf3on5bei.sabkt.gdipper.com/24e3c68b-4042-4988-bfd9-694328a8b52c.jpeg",
                "http://pf3on5bei.sabkt.gdipper.com/1dbbf32d-8a1f-4194-a797-075dfcdbba38.jpeg",
                "http://pf3on5bei.sabkt.gdipper.com/1dbbf32d-8a1f-4194-a797-075dfcdbba38.jpeg",
                "http://pf3on5bei.sabkt.gdipper.com/1dbbf32d-8a1f-4194-a797-075dfcdbba38.jpeg",
                "http://pf3on5bei.sabkt.gdipper.com/1dbbf32d-8a1f-4194-a797-075dfcdbba38.jpeg",
                "http://pf3on5bei.sabkt.gdipper.com/1dbbf32d-8a1f-4194-a797-075dfcdbba38.jpeg",
                "http://pf3on5bei.sabkt.gdipper.com/24e3c68b-4042-4988-bfd9-694328a8b52c.jpeg",
                "http://pf3on5bei.sabkt.gdipper.com/1dbbf32d-8a1f-4194-a797-075dfcdbba38.jpeg",
        };

        for (String postUrl : postImageUrls) {
            Post post = new Post("http://pf3on5bei.sabkt.gdipper.com/profile18.jpg", "Test", postUrl, myLocation, sdf.format(date), "comments");
            postList.add(post);

            date -= 60000;
        }

    }

    public void initView(View view) {
        RecyclerView listView = view.findViewById(R.id.post_list);
        listView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        listView.setAdapter(new PostImageAdapter(this.getContext(), postList));
    }

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

    public void sortByDate() {
        Collections.sort(postList, new Comparator<Post>() {
            @Override
            public int compare(Post post1, Post post2) {
                return post2.getDate().compareTo(post1.getDate());
            }
        });

    }

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
