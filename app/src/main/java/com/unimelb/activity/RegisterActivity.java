package com.unimelb.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.unimelb.constants.CommonConstants;
import com.unimelb.instagramlite.R;
import com.unimelb.net.HttpRequest;
import com.unimelb.net.IResponseHandler;
import com.unimelb.net.ResponseModel;

import org.json.simple.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    private RegisterActivity context;

    private Button registerBtn;

    private boolean isEnableBtn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context = this;

        registerBtn = findViewById(R.id.register_btn);
        registerBtn.setOnClickListener((view) -> {
            EditText usernameEditText = findViewById(R.id.username_edit_text);
            EditText passwordEditText = findViewById(R.id.password_edit_text);
            EditText confirmEditText = findViewById(R.id.confirm_password_edit_text);

            final String username = usernameEditText.getText().toString();
            final String password = passwordEditText.getText().toString();
            final String confirm = confirmEditText.getText().toString();

            if (password.equals(confirm)) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("email", username);
                jsonObject.put("pwd", password);

                HttpRequest.getInstance().doPostRequestAsync(CommonConstants.IP + "/api/v1/register", jsonObject.toJSONString(), new IResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, String errJson) {
                        ResponseModel rm = new ResponseModel(errJson);
                        context.runOnUiThread(() -> {
                            context.toggleLoginButton();
                            Toast.makeText(context, rm.getMsg(), Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onSuccess(String json) {
                        context.runOnUiThread(() -> {
                            Toast.makeText(context, "Register successful", Toast.LENGTH_LONG).show();
                        });
                        // close login activity
                        startActivity(new Intent(context, MainActivity.class));
                        finish();
                    }
                });
            } else {
                Toast.makeText(this, "Two passwords are not equal", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void toggleLoginButton() {
        if (isEnableBtn) {
            registerBtn.setText("Loading...");
            registerBtn.setEnabled(false);
        } else {
            registerBtn.setText("Register");
            registerBtn.setEnabled(true);
        }

        isEnableBtn = !isEnableBtn;
    }

}
