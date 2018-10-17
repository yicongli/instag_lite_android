package com.unimelb.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.unimelb.entity.BasicUserProfile;
import com.unimelb.entity.Comment;
import com.unimelb.entity.Post;
import com.unimelb.instagramlite.R;
import com.unimelb.utils.ImageUtils;

import java.util.List;

public class PostImageAdapter extends RecyclerView.Adapter<PostImageAdapter.ViewHolder>{

    private Context context;
    private LayoutInflater mInflater;
    private List<Post> postList;

    public PostImageAdapter(Context context, List<Post> postList) {
        this.context = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.postList = postList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.view_post_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ImageUtils.loadRoundedImage(context, postList.get(position).getAvatarUrl(), holder.avatarImageView);
        holder.usernameTextView.setText(postList.get(position).getUsername());
        holder.locationTextView.setText(postList.get(position).getLocation().toString());

        ImageUtils.loadImage(context, postList.get(position).getImageUrl(), holder.postImageView);
        holder.dateView.setText(postList.get(position).getDate());
        holder.likeBtn.setText("Like");

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    /**
     * the item to hold the image in the post
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView avatarImageView;
        private TextView usernameTextView;
        private TextView locationTextView;

        private ImageView postImageView;
        private TextView dateView;
        private Button likeBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.post_list_item_avatar);
            usernameTextView = itemView.findViewById(R.id.post_list_item_username);
            locationTextView = itemView.findViewById(R.id.post_list_item_location);

            postImageView = itemView.findViewById(R.id.postImageView);
            dateView = itemView.findViewById(R.id.post_list_item_date);
            likeBtn = itemView.findViewById(R.id.post_list_item_likebutton);
        }
    }
}
