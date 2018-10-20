package com.unimelb.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.unimelb.entity.FollowingActivityItem;
import com.unimelb.instagramlite.R;
import com.unimelb.utils.ImageUtils;

import java.util.List;
/*
*
* A adapter to show the posts of whom user following
* */
public class FollowingActivityListAdapter extends RecyclerView.Adapter<FollowingActivityListAdapter.ViewHolder> {
    /* context */
    private Context context;
    /* list of Following Activity Item */
    private List<FollowingActivityItem> list;

    public FollowingActivityListAdapter(Context context, List<FollowingActivityItem> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.view_activity_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.descTextView.setText(list.get(position).getActivityDesc());
        ImageUtils.loadRoundedImage(context, list.get(position).getAvatarUrl(), holder.avatarImageView);
        if (list.get(position).getPhotoUrl().length() > 0) {
            ImageUtils.loadImage(context, list.get(position).getPhotoUrl(), holder.photoImageView);
        } else {
            holder.photoImageView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        /* user picture ImageView */
        private ImageView avatarImageView;
        /* profile of user TextView */
        private TextView descTextView;
        /* posted photos ImageView */
        private ImageView photoImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.activity_list_item_avatar);
            descTextView = itemView.findViewById(R.id.activity_list_item_desc);
            photoImageView = itemView.findViewById(R.id.activity_list_item_photo);
        }
    }
}
