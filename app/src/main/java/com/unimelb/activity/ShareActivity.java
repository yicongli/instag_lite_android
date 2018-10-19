package com.unimelb.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.unimelb.adapter.ViewPagerAdapter;
import com.unimelb.fragment.CameraFragment;
import com.unimelb.fragment.EffectsFragment;
import com.unimelb.fragment.LibraryFragment;
import com.unimelb.fragment.PostFragment;
import com.unimelb.fragment.ShareFragmentsListener;
import com.unimelb.instagramlite.R;
import com.unimelb.utils.BottomNavigationViewHelper;
import com.unimelb.utils.Permissions;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This activity is the module related to the photo and sharing activity
 */
public class ShareActivity extends AppCompatActivity implements ShareFragmentsListener {
    /**
     * 4 kinds of fragment type in share activity
     */
    enum FragmentType {
        Library,
        Camera,
        Effects,
        Post
    }

    private static final String TAG = "ShareActivity";      // tag of current activity

    private Context mContext        = ShareActivity.this;   // current context
    private BottomNavigationView navigationView;            // the navigation view at the bottom
    private ViewPager viewPager;                            // the pager to store the fragments
    private MenuItem menuItem;                              // the menu item
    private String selectedImagePath;                       // the path of selected photo

    private CameraFragment  cameraFragment;
    private EffectsFragment effectsFragment;
    private PostFragment    postFragment;

    private ArrayList<FragmentType> mViewStack = new ArrayList<>();

    // TODO: check if It works
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
                        viewPager.setCurrentItem(FragmentType.Library.ordinal());    // show library
                        return true;
                    case R.id.navigation_photo:
                        viewPager.setCurrentItem(FragmentType.Camera.ordinal());    // show photo
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

                if(position == FragmentType.Camera.ordinal()) {
                    cameraFragment.startCamera();
                }
                else {
                    cameraFragment.stopCamera();
                }

                // if current show library or camera, then set selected menu button
                if (position < FragmentType.Effects.ordinal()) {
                    menuItem = navigationView.getMenu().getItem(position);
                    menuItem.setChecked(true);

                    // if current show library or camera, update the
                    // bottom view identify according to current type
                    if (mViewStack.size() > 0) {
                        mViewStack.remove(0);
                        int cameraPos = FragmentType.Camera.ordinal();
                        if (position == cameraPos) {
                            mViewStack.add(0, FragmentType.Camera);
                        } else {
                            mViewStack.add(0, FragmentType.Library);
                        }
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        setupViewPager(viewPager);
        navigationView.setSelectedItemId(R.id.navigation_photo);
        mViewStack.add(FragmentType.Camera);
    }

    /**
     * setup the content of viewPager
     * @param viewPager
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        cameraFragment  = CameraFragment.newInstance();
        effectsFragment = EffectsFragment.newInstance();
        postFragment    = PostFragment.newInstance();

        adapter.addFragment(LibraryFragment.newInstance());
        adapter.addFragment(cameraFragment);
        adapter.addFragment(effectsFragment);
        adapter.addFragment(postFragment);

        viewPager.setAdapter(adapter);
    }

    /**
     * show effects fragment when touch next on the library or camera page.
     */
    private void showEffectsFragment()
    {
        viewPager.setCurrentItem(FragmentType.Effects.ordinal(),false);
        // Hide navigationView when show the effect view
        navigationView.setVisibility(View.GONE);
        new Handler().postDelayed(() -> {
                if(effectsFragment != null) {
                    effectsFragment.initUI();
            }
        }, 150);
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

    /**
     * show effect fragment after selecting image on library / camera view
     * @param imagePath file path of selected image
     */
    @Override
    public void showEffectsFragment(String imagePath) {
        selectedImagePath = imagePath;
        showEffectsFragment();

        mViewStack.add(FragmentType.Effects);
    }

    /**
     * return current selected image path
     * @return current selected image path
     */
    @Override
    public String getSelectedImagePath () {
        return  selectedImagePath;
    }

    /**
     * return to previous view
     */
    @Override
    public void backToPreviousView() {
        mViewStack.remove(mViewStack.size()-1);
        FragmentType type = mViewStack.get(mViewStack.size()-1);

        viewPager.setCurrentItem(type.ordinal(),false);

        // Show navigationView when show the library or photo fragment
        if (type.ordinal() < FragmentType.Effects.ordinal()) {
            navigationView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Show post fragment
     * @param imagePath
     */
    @Override
    public void showPostFragment(String imagePath) {
        viewPager.setCurrentItem(FragmentType.Post.ordinal(),false);

        if(postFragment != null) {
            // TODO initiate post fragment
        }

        mViewStack.add(FragmentType.Post);
    }

    /**
     * show corp view
     * @param image source image
     */
    @Override
    public void startCrop (Bitmap image) {
        Uri uri = getImageUri(ShareActivity.this, image);
        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), "temCropImage.png")));
        uCrop.useSourceImageAspectRatio();
        uCrop.start(ShareActivity.this, UCrop.REQUEST_CROP);
    }

    /**
     * Generate Uri for bitmap image
     * @param inContext context
     * @param inImage image source
     * @return Uri
     */
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    /**
     * Get data from crop view
     * @param requestCode request code
     * @param resultCode  result code
     * @param data        data from crop view
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode, data);

        if (requestCode == UCrop.REQUEST_CROP) {
            if (resultCode == RESULT_OK) {
                final Uri resultUri = UCrop.getOutput(data);
                try {
                    Bitmap image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    effectsFragment.setImage(image);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
