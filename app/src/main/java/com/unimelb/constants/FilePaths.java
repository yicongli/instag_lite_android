package com.unimelb.constants;

import android.os.Environment;
/*
*
* FilePaths class to storage the filePath parameter
* */
public class FilePaths {

    // get the root directory
    public static String ROOT_DIRECTORY = Environment.getExternalStorageDirectory().getPath();

    // "/storage/emulated/0/Pictures"
    public static String PICTURE_PATH = ROOT_DIRECTORY + "/Pictures";

    // "/storage/emulated/0/DCIM/camera"
    public static String CAMERA_PATH = ROOT_DIRECTORY + "/DCIM";


}
