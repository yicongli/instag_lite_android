package com.unimelb.component;

import android.graphics.Bitmap;

import com.zomato.photofilters.SampleFilters;
import com.zomato.photofilters.imageprocessors.Filter;

/**
 * The class for holding the picture of each effect item
 */
public class ThumbnailItem {
    public Bitmap image;
    public Filter filter;
    public String name;

    public ThumbnailItem(String name, Bitmap image, Filter filter) {
        this.name = name;
        this.filter = filter;
        if(filter == null)
            this.image = image;
        else{
            this.image = filter.processFilter(Bitmap.createBitmap(image));
        }
    }
}
