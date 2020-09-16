package com.ck.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ck.R;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.ArrayList;
import java.util.List;

public class RecommendListAdapter extends RecyclerView.Adapter<RecommendListAdapter.InnerHolder> {
    private List<Album> mData = new ArrayList<>();

    @NonNull
    @Override
    public RecommendListAdapter.InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend, parent, false);
        return new InnerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendListAdapter.InnerHolder holder, int position) {
        /*itemView在ViewHolder中已经保存，在onCreateViewHolder中的layout就是*/
        holder.itemView.setTag(position);//设置标记
        holder.setData(mData.get(position));//设置数据
    }

    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }

    public void setData(List<Album> albumList) {
        if (mData != null) {
            mData.clear();
            mData.addAll(albumList);
        }
        notifyDataSetChanged();//更新UI
    }

    public static class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }

        @SuppressLint("SetTextI18n")
        public void setData(Album album) {
            //找到每个控件，并设置数值
            ImageView image = itemView.findViewById(R.id.recommend_item_image);
            TextView title = itemView.findViewById(R.id.recommend_item_title);
            TextView content = itemView.findViewById(R.id.recommend_item_content);
            TextView playCount = itemView.findViewById(R.id.recommend_item_play_count);
            TextView playEpisode = itemView.findViewById(R.id.recommend_item_play_episode);
            Glide.with(itemView.getContext()).load(album.getCoverUrlLarge()).into(image);
            title.setText(album.getAlbumTitle());
            content.setText(album.getAlbumIntro());
            playCount.setText(album.getPlayCount() + "次");
            playEpisode.setText(album.getIncludeTrackCount() + "集");
        }
    }
}
