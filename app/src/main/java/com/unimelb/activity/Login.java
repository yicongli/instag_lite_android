package com.unimelb.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.unimelb.instagramlite.R;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class Login extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    public void goToRegister(View view){
        Intent intent = new Intent(this, RegisterActivity.class);

        startActivity(intent);
    }
}
