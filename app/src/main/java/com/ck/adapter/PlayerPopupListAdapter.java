package com.ck.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ck.R;
import com.ck.base.BaseApplication;
import com.ck.view.PlayerListPopupWindow;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.ArrayList;
import java.util.List;

public class PlayerPopupListAdapter extends RecyclerView.Adapter<PlayerPopupListAdapter.InnerHolder> {

    private List<Track> mData = new ArrayList<>();
    private int mPlayingIndex = 0;
    private PlayerListPopupWindow.PlayListItemClickListener mItemClickListener = null;

    @NonNull
    @Override
    public PlayerPopupListAdapter.InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_player_pupup_list, parent, false);
        return new InnerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerPopupListAdapter.InnerHolder holder, final int position) {
        //点击播放列表的歌曲，切歌
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(position);
                }
            }
        });
        //设置数据
        Track track = mData.get(position);
        TextView title = holder.itemView.findViewById(R.id.player_popup_item_title);
        title.setText(track.getTrackTitle());
        //设置当前播放节目的字体颜色和图标
        if (mPlayingIndex == position) {
            ImageView playingIcon = holder.itemView.findViewById(R.id.player_popup_item_playing_icon);
            playingIcon.setVisibility(View.VISIBLE);
            title.setTextColor(BaseApplication.getAppContext().getResources().getColor(R.color.colorMain));
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<Track> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * 改变当前播放的节目
     */
    public void setCurrentPlayPosition(int position) {
        mPlayingIndex = position;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(PlayerListPopupWindow.PlayListItemClickListener listener) {
        mItemClickListener = listener;
    }

    public static class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
