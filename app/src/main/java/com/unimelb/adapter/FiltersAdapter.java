package com.unimelb.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.unimelb.component.ThumbnailItem;
import com.unimelb.fragment.EffectsFragment;
import com.unimelb.instagramlite.R;
import com.zomato.photofilters.imageprocessors.Filter;

import java.util.List;

/**
 * Created by luca1897 on 25/06/17.
 */

public class FiltersAdapter extends RecyclerView.Adapter<FiltersAdapter.ViewHolder> {

    private int selected = 0;
    private List<ThumbnailItem> images;
    private EffectsFragment effectsFragment;


    public FiltersAdapter(EffectsFragment effectsFragment, List<ThumbnailItem> images) {
        this.effectsFragment = effectsFragment;
        this.images = images;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_effect_item, parent, false);


        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.filterName.setText(images.get(position).name);
        holder.imageView.setImageBitmap(images.get(position).image);
        holder.imageView.setOnClickListener(View -> {
                effectsFragment.setImage(images.get(position).image);
                selected = position;
                notifyDataSetChanged();
        });

        if(selected == position)
        {
            holder.filterName.setTextColor(Color.BLACK);
        }else{
            holder.filterName.setTextColor(Color.GRAY);
        }
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public ImageView imageView;
        private TextView filterName;
        private ViewHolder(final View v) {
            super(v);
            imageView  = v.findViewById(R.id.imageView);
            filterName = v.findViewById(R.id.filter_name);
            view = v;
        }
    }

    public Filter getFilterSelected() {
        return images.get(selected).filter;
    }
}
