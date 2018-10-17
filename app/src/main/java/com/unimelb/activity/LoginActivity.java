package com.unimelb.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.unimelb.constants.CommonConstants;
import com.unimelb.instagramlite.R;
import com.unimelb.net.HttpRequest;
import com.unimelb.net.IResponseHandler;
import com.unimelb.net.ResponseModel;

import org.json.simple.JSONObject;

/**
 * Login Activity
 */
public class LoginActivity extends AppCompatActivity {
    private Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        initView();
    }

    /**
     * Initialise the view component and event listener
     */
    private void initView() {
        findViewById(R.id.login_btn).setOnClickListener((view) -> {
            // Get edit text content
            EditText userNameEditText = findViewById(R.id.username_edit_text);
            EditText passwordEditText = findViewById(R.id.password_edit_text);
            final String username = userNameEditText.getText().toString();
            final String password = passwordEditText.getText().toString();

            try {
                JSONObject obj = new JSONObject();
                obj.put("email", username);
                obj.put("pwd", password);
                System.out.println(obj.toJSONString());

                HttpRequest.getInstance().doPostRequestAsync(CommonConstants.IP + "/api/v1/login", obj.toJSONString(), new IResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, String errJson) {
//                        System.out.println(errJson);
                        ResponseModel rm = new ResponseModel(errJson);
                        context.runOnUiThread(() -> Toast.makeText(context, rm.getMsg(), Toast.LENGTH_SHORT).show());
                    }

                    @Override
                    public void onSuccess(String json) {
//                        System.out.println(json);
                        ResponseModel rm = new ResponseModel(json);
                        JSONObject data = rm.getData();
                        String token = (String) data.get("token");

                        // save the token
                        CommonConstants.token = token;
                        System.out.println(token);
                        context.runOnUiThread(() -> Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Something going wrong, please try later.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Navigate to register activity
     *
     * @param view
     */
    public void goToRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);

        startActivity(intent);
    }
}
