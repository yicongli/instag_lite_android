package com.unimelb.utils;

import android.Manifest;
/*
* Permissions class to apply app permission needed
* */
public class Permissions {

    public static final String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA

    };
}
