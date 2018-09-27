package com.unimelb.utils;

import android.os.Environment;

public class FilePaths {

    // get the root directory
    public String ROOT_DIRECTORY = Environment.getExternalStorageDirectory().getPath();

    // "/storage/emulated/0/Pictures"
    public String PICTURE_PATH = ROOT_DIRECTORY + "/Pictures";

    // "/storage/emulated/0/DCIM/camera"
    public String CAMERA_PATH = ROOT_DIRECTORY + "/DCIM/camera";


}
