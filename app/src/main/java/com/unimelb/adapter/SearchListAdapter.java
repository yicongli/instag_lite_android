package com.unimelb.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.unimelb.entity.BasicUserProfile;
import com.unimelb.instagramlite.R;
import com.unimelb.utils.ImageUtils;

import java.util.List;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> {

    private Context context;
    private List<BasicUserProfile> list;

    public SearchListAdapter(Context context, List<BasicUserProfile> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.view_search_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.titleTextView.setText(list.get(position).getUsername());
        holder.subtitleTextView.setText(list.get(position).getBio());
        ImageUtils.loadRoundedImage(context, list.get(position).getAvatarUrl(), holder.avatarImageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView avatarImageView;
        private TextView titleTextView;
        private TextView subtitleTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.search_list_item_avatar);
            titleTextView = itemView.findViewById(R.id.search_list_item_title);
            subtitleTextView = itemView.findViewById(R.id.search_list_item_subtitle);
        }
    }
}
