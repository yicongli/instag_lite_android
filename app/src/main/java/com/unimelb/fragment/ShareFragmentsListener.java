package com.unimelb.fragment;

import android.graphics.Bitmap;

/**
 * This interface must be implemented by activities that contain this
 * fragment to allow an interaction in this fragment to be communicated
 * to the activity and potentially other fragments contained in that
 * activity.
 * <p>
 * See the Android Training lesson <a href=
 * "http://developer.android.com/training/basics/fragments/communicating.html"
 * >Communicating with Other Fragments</a> for more information.
 */

public interface ShareFragmentsListener {

    // show EffectsFragment after selecting image in Library/Camera fragment
    void showEffectsFragment(String imagePath);

    // get selected image path
    String getSelectedImagePath();

    // go back to previous shown view
    void backToPreviousView();

    // show Post Fragment
    void showPostFragment(String imagePath);

    // show crop activity
    void startCrop(Bitmap image);
}
