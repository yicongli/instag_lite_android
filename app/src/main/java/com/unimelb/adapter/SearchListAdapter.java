package com.unimelb.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.unimelb.constants.CommonConstants;
import com.unimelb.entity.BasicUserProfile;
import com.unimelb.instagramlite.R;
import com.unimelb.net.ErrorHandler;
import com.unimelb.net.HttpRequest;
import com.unimelb.net.IResponseHandler;
import com.unimelb.net.ResponseModel;
import com.unimelb.utils.ImageUtils;

import java.util.List;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> {

    private Activity context;
    private List<BasicUserProfile> list;

    public SearchListAdapter(Activity context, List<BasicUserProfile> list) {
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
        holder.followBtn.setOnClickListener((view) -> {
            holder.followBtn.setEnabled(false);
            HttpRequest.getInstance().doGetRequestAsync(CommonConstants.IP + "/api/v1/users/self/follows", null, new IResponseHandler() {
                @Override
                public void onFailure(int statusCode, String errJson) {
                    new ErrorHandler(context).handle(statusCode, errJson);
                    context.runOnUiThread(() -> holder.followBtn.setEnabled(true));
                }

                @Override
                public void onSuccess(String json) {
                    context.runOnUiThread(() -> {
                        Toast.makeText(context, "Follow user successful", Toast.LENGTH_LONG).show();
                        holder.followBtn.setBackgroundColor(context.getResources().getColor(R.color.lightGrey));
                        holder.followBtn.setEnabled(false);
                    });
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView avatarImageView;
        private TextView titleTextView;
        private TextView subtitleTextView;
        private Button followBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.search_list_item_avatar);
            titleTextView = itemView.findViewById(R.id.search_list_item_title);
            subtitleTextView = itemView.findViewById(R.id.search_list_item_subtitle);
            followBtn = itemView.findViewById(R.id.search_list_item_follow_btn);
        }
    }
}
