package com.unimelb.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.unimelb.activity.CommentsActivity;
import com.unimelb.activity.LikesActivity;
import com.unimelb.constants.CommonConstants;
import com.unimelb.entity.Post;
import com.unimelb.instagramlite.R;
import com.unimelb.net.ErrorHandler;
import com.unimelb.net.HttpRequest;
import com.unimelb.net.IResponseHandler;
import com.unimelb.utils.ImageUtils;

import java.util.List;

public class PostImageAdapter extends RecyclerView.Adapter<PostImageAdapter.ViewHolder> {

    private Activity context;
    private List<Post> postList;

    public PostImageAdapter(Activity context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(context, LayoutInflater.from(context).inflate(R.layout.view_post_list_item, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ImageUtils.loadRoundedImage(context, postList.get(position).getAvatarUrl(), holder.avatarImageView);
        holder.usernameTextView.setText(postList.get(position).getUsername());
//        holder.locationTextView.setText(postList.get(position).getLocation().toString());

        ImageUtils.loadImage(context, postList.get(position).getImageUrl(), holder.postImageView);
        holder.dateView.setText(postList.get(position).getDate());

        ImageUtils.loadRoundedImage(context, "http://pgr1ie9ou.sabkt.gdipper.com/di.jpg", holder.ownerImageView);

        holder.likeCountLabel.setText(postList.get(position).getLikesCount() + " likes");
        holder.commentBtn.setText("View all " + postList.get(position).getCommentsCount() + " comments");

        holder.likeBtn.setOnClickListener((view) -> {
            HttpRequest.getInstance().doPostRequestAsync(CommonConstants.IP + "/api/v1/media/" + postList.get(position).getPostId() + "/likes", null, new IResponseHandler() {
                @Override
                public void onFailure(int statusCode, String errJson) {
                    new ErrorHandler(context).handle(statusCode, errJson);
                }

                @Override
                public void onSuccess(String json) {
                    context.runOnUiThread(() -> {
                        Toast.makeText(context, "You like this picture", Toast.LENGTH_SHORT).show();
                        holder.likeCountLabel.setText(postList.get(position).getLikesCount() + 1 + " likes");
                        holder.likeBtn.setImageResource(R.drawable.ic_favorite_black_24dp);
                    });
                }
            });
        });
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
        private ImageView likeBtn;
        private TextView likeCountLabel;
        private ImageView ownerImageView;
        private TextView commentBtn;

        public ViewHolder(Context context, View itemView) {
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.post_list_item_avatar);
            usernameTextView = itemView.findViewById(R.id.post_list_item_username);
            locationTextView = itemView.findViewById(R.id.post_list_item_location);
            postImageView = itemView.findViewById(R.id.post_image_view);
            dateView = itemView.findViewById(R.id.post_list_item_date);
            likeBtn = itemView.findViewById(R.id.post_list_item_like_btn);
            likeCountLabel = itemView.findViewById(R.id.post_list_item_like_label);
            ownerImageView = itemView.findViewById(R.id.post_list_item_owner_avatar);
            commentBtn = itemView.findViewById(R.id.post_list_item_view_comment_btn);
            EditText commentEditText = itemView.findViewById(R.id.post_list_item_comment_edit);
            TextView postBtn = itemView.findViewById(R.id.post_list_item_post_btn);

            likeCountLabel.setOnClickListener((view) -> {
                context.startActivity(new Intent(context, LikesActivity.class));
            });

            commentBtn.setOnClickListener((view) -> {
                context.startActivity(new Intent(context, CommentsActivity.class));
            });

            // detect edit text focus event, when it focused, display post button
            commentEditText.setOnFocusChangeListener((view, hasFocus) -> {
                if (hasFocus) {
                    postBtn.setVisibility(View.VISIBLE);
                } else {
                    postBtn.setVisibility(View.GONE);
                }
            });
        }
    }
}
