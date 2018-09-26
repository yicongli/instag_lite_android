package com.unimelb.component;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.unimelb.instagramlite.R;

/**
 * Custom a list item for search result list
 */
public class SearchListItem extends LinearLayout{
    /** outer linear layout */
    private LinearLayout wrapperLinearLayout;

    /** image view to set avatar*/
    private ImageView avatarImageView;

    /** text view to set title */
    private TextView titleTextView;

    /** text view to set subtitle */
    private TextView subtitleTextView;

    public SearchListItem(Context context) {
        super(context);
        initView(context);
    }

    public SearchListItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SearchListItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    /**
     * init view by find view id
     * @param context
     */
    public void initView(Context context){
        LayoutInflater.from(context).inflate(R.layout.view_search_list_item, this, true);
        wrapperLinearLayout = findViewById(R.id.search_list_item);
        avatarImageView = findViewById(R.id.search_list_item_avatar);
        titleTextView = findViewById(R.id.search_list_item_title);
        subtitleTextView = findViewById(R.id.search_list_item_subtitle);
    }

    /**
     * set title value
     * @param string
     */
    public void setTitle(String string){
        titleTextView.setText(string);
    }

    /**
     * set subtitle value
     * @param string
     */
    public void setSubTitle(String string){
        subtitleTextView.setText(string);
    }

    /**
     * set avatar value
     * @param uri
     */
    public void setAvatar(Uri uri){
        avatarImageView.setImageURI(uri);
    }

    /**
     * set onClick listener
     * @param onClickListener
     */
    public void setOnClickListener(OnClickListener onClickListener){
        wrapperLinearLayout.setOnClickListener(onClickListener);
    }
}
