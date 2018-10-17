package com.unimelb.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unimelb.entity.Comment;
import com.unimelb.instagramlite.R;

import org.w3c.dom.Text;

import java.util.List;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater mInflater;
    private List<Comment> commentList;

    public CommentListAdapter(Context context, List<Comment> commentList) {
        this.context = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.view_post_comments_items,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.usernameTextView.setText(commentList.get(position).getUsername());
        holder.commentTextView.setText(commentList.get(position).getComment());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView usernameTextView;
        private TextView commentTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.post_comment_username);
            commentTextView = itemView.findViewById(R.id.post_comment);
        }
    }
}
