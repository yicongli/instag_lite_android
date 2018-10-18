package com.unimelb.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.unimelb.constants.CommonConstants;

/**
 * Token management
 */
public class TokenHelper {

    /**
     * Context
     */
    private Context context;

    /**
     * SharedPreferences instance
     **/
    private SharedPreferences sp;

    public TokenHelper(Context context) {
        this.context = context;
        sp = context.getSharedPreferences("settings", Activity.MODE_PRIVATE);
    }

    /**
     * Save token in the sharedPreference
     *
     * @param token
     */
    public void saveToken(String token) {
        CommonConstants.token = token;
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("token", token);
        editor.apply();
    }

    /**
     * Get token
     *
     * @return
     */
    public String getToken() {
        return sp.getString("token", "");
    }

    /**
     * Validate whether the token is valid
     *
     * @return
     */
    public boolean isValidToken() {
        String token = sp.getString("token", "");
        System.out.println(token);
        CommonConstants.token = token;
        return token.length() > 0;
    }

    /**
     * Remove token value
     */
    public void deleteToken() {
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("token");
        editor.apply();
    }
}
