package com.unimelb.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.unimelb.entity.Comment;
import com.unimelb.instagramlite.R;
import com.unimelb.utils.ImageUtils;

import java.util.List;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder> {

    private Context context;
    private List<Comment> commentList;

    public CommentListAdapter(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.view_comment_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.commentTv.setText(commentList.get(position).getComment());
        holder.dateTv.setText(commentList.get(position).getDate());
        ImageUtils.loadRoundedImage(context, commentList.get(position).getAvatarUrl(), holder.avatarIv);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView commentTv;
        private ImageView avatarIv;
        private TextView dateTv;

        public ViewHolder(View itemView) {
            super(itemView);
            commentTv = itemView.findViewById(R.id.comment_item_content);
            avatarIv = itemView.findViewById(R.id.comment_item_avatar);
            dateTv = itemView.findViewById(R.id.comment_item_date);
        }
    }
}
