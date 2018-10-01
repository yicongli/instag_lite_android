package com.unimelb.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.unimelb.component.GridViewItem;
import com.unimelb.utils.ImageUtils;

public class SquareImageAdapter extends BaseAdapter {
    private Context context;
    private String[] images;

    public SquareImageAdapter(Context context, String[] images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return images[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup group) {
        GridViewItem imageView = null;
        if (view == null) {
            imageView = new GridViewItem(context);
        } else {
            imageView = (GridViewItem) view;
        }
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        ImageUtils.loadImage(context, images[position], imageView);
        return imageView;
    }
}
