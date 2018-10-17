package com.unimelb.utils;

import android.os.Environment;

public class FilePaths {

    // get the root directory
    public static String ROOT_DIRECTORY = Environment.getExternalStorageDirectory().getPath();

    // "/storage/emulated/0/Pictures"
    public static String PICTURE_PATH = ROOT_DIRECTORY + "/Pictures";

    // "/storage/emulated/0/DCIM/camera"
    public static String CAMERA_PATH = ROOT_DIRECTORY + "/DCIM/camera";


}
