package com.unimelb.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.unimelb.instagramlite.R;
/*
* A activity to show the welcome information for a new user.
* Including a statement for copy right.
* */
public class WelcomeActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }
}
