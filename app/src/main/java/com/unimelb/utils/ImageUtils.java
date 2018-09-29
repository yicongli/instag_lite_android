package com.unimelb.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.unimelb.instagramlite.R;

public class ImageUtils {


    public static void loadImage(Context context, String url, ImageView imageView){
        Glide.with(context)
                .load(url)
                .apply(RequestOptions.placeholderOf(R.mipmap.image_placeholder))
                .into(imageView);
    }
}
