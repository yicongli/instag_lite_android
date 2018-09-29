package com.unimelb.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.unimelb.adapter.ViewPagerAdapter;
import com.unimelb.base.BaseFragment;
import com.unimelb.fragment.DiscoverFragment;
import com.unimelb.fragment.ProfileFragment;
import com.unimelb.instagramlite.R;
import com.unimelb.utils.BottomNavigationViewHelper;

/**
 * Main portal
 */
public class MainActivity extends AppCompatActivity {
    private BottomNavigationView navigationView;
    private ViewPager viewPager;
    private MenuItem menuItem;

    private Context mContext = MainActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        loginAuth();

        viewPager = findViewById(R.id.viewpager);
        navigationView = findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigationView);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

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
                        viewPager.setCurrentItem(3);
                        return true;
                    case R.id.navigation_profile:
                        viewPager.setCurrentItem(4);
                        return true;
                }
                return false;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

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
    private void loginAuth(){

        startActivity(new Intent(this, Login.class));
        finish();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(BaseFragment.newInstance("home"));
        adapter.addFragment(new DiscoverFragment());
        adapter.addFragment(BaseFragment.newInstance("favourite"));
        adapter.addFragment(new ProfileFragment());
        viewPager.setAdapter(adapter);
    }
}
