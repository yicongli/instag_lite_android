package com.unimelb.component;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
/* Customized GridViewItem for show pictures in a grid*/
public class GridViewItem extends android.support.v7.widget.AppCompatImageView {
    public GridViewItem(Context context) {
        super(context);
    }

    public GridViewItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GridViewItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }
}
