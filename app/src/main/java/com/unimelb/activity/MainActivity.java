package com.unimelb.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.unimelb.adapter.ViewPagerAdapter;
import com.unimelb.constants.CommonConstants;
import com.unimelb.fragment.ActivityFragment;
import com.unimelb.fragment.DiscoverFragment;
import com.unimelb.fragment.HomeFragment;
import com.unimelb.fragment.ProfileFragment;
import com.unimelb.instagramlite.R;
import com.unimelb.utils.BottomNavigationViewHelper;
import com.unimelb.utils.LocationUtils;
import com.unimelb.utils.TokenHelper;

/**
 * Main portal
 */
public class MainActivity extends AppCompatActivity {
    /* navigation view to show a list of button for different fragments*/
    private BottomNavigationView navigationView;
    /* viewPager */
    private ViewPager viewPager;
    /* menuItem */
    private MenuItem menuItem;
    /* mContext */
    private Context mContext = MainActivity.this;
    /* location permission */
    static public final int REQUEST_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Check permission
            ActivityCompat.requestPermissions(this,
            new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}
            , REQUEST_LOCATION);
        } else {
            initGPS();
        }

        loginAuth();

        viewPager = findViewById(R.id.viewpager);
        navigationView = findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigationView);
        navigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        viewPager.setCurrentItem(0);
                        return true;
                    case R.id.navigation_discover:
                        viewPager.setCurrentItem(1);
                        return true;
                    case R.id.navigation_share:
                        Intent intent1 = new Intent(mContext, ShareActivity.class);
                        mContext.startActivity(intent1);
                        return false;
                    case R.id.navigation_favourite:
                        viewPager.setCurrentItem(2);
                        return true;
                    case R.id.navigation_profile:
                        viewPager.setCurrentItem(3);
                        return true;
                }
                return false;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    navigationView.getMenu().getItem(0).setChecked(false);
                }
                menuItem = navigationView.getMenu().getItem(position);
                menuItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        setupViewPager(viewPager);
    }

    /**
     * user login logic, if there is not access token, navigate to login page
     */
    private void loginAuth() {
        TokenHelper th = new TokenHelper(this);
//        th.deleteToken();
        if (!th.isValidToken()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    /** Setup a ViewPager including:
     * HomeFragment
     * DiscoverFragment
     * ActivityFragment
     * ProfileFragment
     * */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new HomeFragment());
        adapter.addFragment(new DiscoverFragment());
        adapter.addFragment(new ActivityFragment());
        adapter.addFragment(new ProfileFragment());
        viewPager.setAdapter(adapter);
    }

    /*
     * Initialize Gps function
     * */
    private void initGPS() {
        LocationUtils.initLocation(this);
        CommonConstants.latitude = LocationUtils.latitude;
        CommonConstants.longitude = LocationUtils.longitude;
//        Toast.makeText(this, LocationUtils.longitude + "", Toast.LENGTH_LONG).show();
//        Toast.makeText(this, LocationUtils.latitude + "", Toast.LENGTH_LONG).show();
    }

    /**
     * Request the location permission
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if(grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initGPS();
            } else {
                // Permission was denied or request was cancelled
            }
        }
    }
}
