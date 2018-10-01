package com.unimelb.utils;

import android.content.Context;
import android.widget.ImageView;
import com.unimelb.instagramlite.R;

/**
 * Image loader helper
 */
public class ImageUtils {

    /**
     * Load image
     * @param context: context
     * @param url: image url
     * @param imageView: image view you want to render
     */
    public static void loadImage(Context context, String url, ImageView imageView){
        GlideApp.with(context)
                .load(url)
                .placeholder(R.mipmap.image_placeholder)
                .into(imageView);
    }

    /**
     * Load a rounded image
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadRoundedImage(Context context, String url, ImageView imageView){
        GlideApp.with(context)
                .load(url)
                .placeholder(R.mipmap.image_placeholder)
                .circleCrop()
                .into(imageView);
    }
}
