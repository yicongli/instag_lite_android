package com.unimelb.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.unimelb.constants.CommonConstants;
import com.unimelb.instagramlite.R;
import com.unimelb.net.HttpRequest;
import com.unimelb.net.IResponseHandler;
import com.unimelb.net.ResponseModel;
import com.unimelb.utils.TokenHelper;

import org.json.simple.JSONObject;

/**
 * Login Activity
 */
public class LoginActivity extends AppCompatActivity {
    private LoginActivity context;

    private Button loginBtn;

    private boolean isEnableBtn = true;

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
        loginBtn = findViewById(R.id.login_btn);
        loginBtn.setOnClickListener((view) -> {
            this.toggleLoginButton();
            // Get edit text content
            EditText userNameEditText = findViewById(R.id.username_edit_text);
            EditText passwordEditText = findViewById(R.id.password_edit_text);
            final String username = userNameEditText.getText().toString();
            final String password = passwordEditText.getText().toString();

            try {
                // generate json body
                JSONObject obj = new JSONObject();
                obj.put("email", username);
                obj.put("pwd", password);
                System.out.println(obj.toJSONString());

                HttpRequest.getInstance().doPostRequestAsync(CommonConstants.IP + "/api/v1/login", obj.toJSONString(), new IResponseHandler() {

                    @Override
                    public void onFailure(int statusCode, String errJson) {
//                        System.out.println(errJson);
                        ResponseModel rm = new ResponseModel(errJson);
                        context.runOnUiThread(() -> {
                            context.toggleLoginButton();
                            Toast.makeText(context, rm.getMsg(), Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onSuccess(String json) {
//                        System.out.println(json);
                        ResponseModel rm = new ResponseModel(json);
                        JSONObject data = rm.getData();
                        String token = (String) data.get("token");

                        // save the token
                        TokenHelper th = new TokenHelper(context);
                        th.saveToken(token);
                        CommonConstants.token = token;
//                        System.out.println(token);

                        // update UI
                        context.runOnUiThread(() -> {
                            Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show();
                        });
                        // close login activity
                        startActivity(new Intent(context, MainActivity.class));
                        finish();
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
        startActivity(new Intent(this, RegisterActivity.class));
    }

    private void toggleLoginButton() {
        if (isEnableBtn) {
            loginBtn.setText("Login...");
            loginBtn.setEnabled(false);
        } else {
            loginBtn.setText("Login");
            loginBtn.setEnabled(true);
        }

        isEnableBtn = !isEnableBtn;
    }
}
