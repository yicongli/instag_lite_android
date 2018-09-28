package com.unimelb.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.unimelb.instagramlite.R;

public class RegisterActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Intent intent = getIntent();
    }

    public void register(View view)
    {
        EditText userName = (EditText) findViewById(R.id.userName_EditText);
        String userName_string = userName.getText().toString();
        EditText password = (EditText) findViewById(R.id.password_EditText);
        String password_string = password.getText().toString();
        TextView confirmPassword = (TextView) findViewById(R.id.confirm_password_EditText);
        String confirmPassword_string = confirmPassword.getText().toString();

        //if unserName already register by other user
        //boolean isExist=verifyUserName(userName_string);
        boolean isExist = userName_string.equals("123");
        if (isExist)
        {
            popDialog1("Account already exist.","Please change your account name.");

        }

        //password != confirm password, pop up a dialog and re-input
        if (password_string.equals(confirmPassword_string))
        {
            //go to welcome activity
            Intent intent = new Intent(this, WelcomeActivity.class);

            startActivity(intent);
        }
        else
        {
            popDialog1("Password invalid.",
                    "Please make sure the password you type in are exactly the same between two times.");

        }
    }

    public void popDialog1(String title, String content)
    {
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle(title)
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {

                    }
                }).setMessage(content).create();
        dialog.show();
    }

}
