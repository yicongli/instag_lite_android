package com.unimelb.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.unimelb.constants.CommonConstants;
import com.unimelb.instagramlite.R;
import com.unimelb.net.HttpRequest;
import com.unimelb.net.IResponseHandler;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Login Activity
 */
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        JSONObject obj = new JSONObject();
        try {
            obj.put("email", "jg@jg.com");
            obj.put("pwd", "jinge");
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpRequest.getInstance().doPostRequestAsync(CommonConstants.IP + "/api/v1/login", obj.toString(), new IResponseHandler() {
            @Override
            public void onFailure(int statusCode, String errMsg) {
                System.out.println(errMsg);
            }

            @Override
            public void onSuccess(String json) {
                System.out.println(json);
                JSONParser parser = new JSONParser();
                try {
                    JSONObject obj = (JSONObject)parser.parse(json);
                    JSONObject data = (JSONObject)obj.get("data");
                    String token = (String)data.get("token");
                    System.out.println(token);
                } catch (Exception e) {
                    e.printStackTrace();
                }

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
