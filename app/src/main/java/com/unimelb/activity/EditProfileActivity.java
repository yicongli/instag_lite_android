package com.unimelb.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.unimelb.instagramlite.R;

/*
 * An activity for user edit his own profile page content.
 */
public class EditProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
    }
}
