package com.unimelb.activity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.unimelb.adapter.ViewPagerAdapter;
import com.unimelb.fragment.CameraFragment;
import com.unimelb.fragment.LibraryFragment;
import com.unimelb.fragment.ShareFragmentsListener;
import com.unimelb.instagramlite.R;
import com.unimelb.utils.BottomNavigationViewHelper;
import com.unimelb.utils.Permissions;

/**
 * This activity is the module related to the photo and sharing activity
 */
public class ShareActivity extends AppCompatActivity implements ShareFragmentsListener{
    private static final String TAG = "ShareActivity";      // tag of current activity
    private Context mContext        = ShareActivity.this;   // current context
    private BottomNavigationView navigationView;            // the navigation view at the bottom
    private ViewPager viewPager;                            // the pager to store the fragments
    private MenuItem menuItem;                              // the menu item

    private static final int VERIFY_PERMISSION_REQUEST = 1; // the flag of verify permission request

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        Log.d(TAG, "onCreate: started");

        // check camera related permissions
        if (!checkPermissionsArray(Permissions.PERMISSIONS)) {
            verifyPermissionsArray(Permissions.PERMISSIONS);
        }

        // setup viewPager
        viewPager = findViewById(R.id.viewpager_camera);
        navigationView = findViewById(R.id.navigation_camera);
        BottomNavigationViewHelper.disableShiftMode(navigationView);

        // the listener to handle the selecting operation of bottom navigation view
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_library:
                        viewPager.setCurrentItem(0);    // show library
                        return true;
                    case R.id.navigation_photo:
                        viewPager.setCurrentItem(1);    // show photo
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
                }
                else {
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
        navigationView.setSelectedItemId(R.id.navigation_photo);
    }

    /**
     * setup the content of viewPager
     * @param viewPager
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(LibraryFragment.newInstance());
        adapter.addFragment(CameraFragment.newInstance());
        viewPager.setAdapter(adapter);
    }

    /**
     * verify all permissions
     * @param permissions the permission identity
     */
    public void verifyPermissionsArray(String[] permissions) {
        Log.d(TAG, "verify permissions");
        ActivityCompat.requestPermissions(this, permissions, VERIFY_PERMISSION_REQUEST);
    }

    /**
     * Check all permission granted status
     * @param permissions the permission identity array
     * @return
     */
    public boolean checkPermissionsArray (String[] permissions) {
        Log.d(TAG, "check permissions");
        for (int i = 0; i < permissions.length; i++) {
            String check = permissions[i];
            if (!checkPermission(check)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Check if single permission has been granted
     * @param permission the permission that checked here
     * @return true granted / false not granted
     */
    public boolean checkPermission (String permission) {
        Log.d(TAG, "check permission" + permission);

        int permissionRequest = ActivityCompat.checkSelfPermission(mContext, permission);

        if (permissionRequest != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, permission + "not granted");
            return false;
        }
        else {
            Log.d(TAG, permission + "granted");
            return true;
        }
    }

    @Override
    public void selectedImage(String imagePath) {
        // TODO: jump to filter fragment
    }
}
