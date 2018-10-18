package com.unimelb.net;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

public class ErrorHandler {
    private Activity context;

    public ErrorHandler(Activity context) {
        this.context = context;
    }

    public void handle(int statusCode, String errJson) {
        Log.e("ERR", errJson);
        if (statusCode == -1) {
            context.runOnUiThread(() -> {
                Toast.makeText(context, "Server connecting error", Toast.LENGTH_SHORT).show();
            });
        } else {
            Log.e("ERR", errJson);
            ResponseModel rm = new ResponseModel(errJson);
            context.runOnUiThread(() -> {
                Toast.makeText(context, rm.getMsg(), Toast.LENGTH_SHORT).show();
            });
        }
    }
}
