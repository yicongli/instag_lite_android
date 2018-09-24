package com.unimelb.activity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.unimelb.instagramlite.R;
import com.unimelb.utils.Permissions;

public class ShareActivity extends AppCompatActivity {
    private static final String TAG = "ShareActivity";
    private Context mContext = ShareActivity.this;

    //Constants
    private static final int ACTIVITY_NUM = 2;
    private static final int VERIFY_PERMISSION_REQUEST = 1;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_library:
                    return true;
                case R.id.navigation_photo:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_camera);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Log.d(TAG, "onCreate: started");

        // check camera related permissions
        if (!checkPermissionsArray(Permissions.PERMISSIONS)) {
            verifyPermissionsArray(Permissions.PERMISSIONS);
        }
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

}
