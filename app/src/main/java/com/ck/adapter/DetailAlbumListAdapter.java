package com.ck.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ck.R;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DetailAlbumListAdapter extends RecyclerView.Adapter<DetailAlbumListAdapter.InnerHolder> {

    private List<Track> mTracks = new ArrayList<>();
    private SimpleDateFormat mDatetimeFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat mTimeFormat = new SimpleDateFormat("mm:ss");
    private ItemClickListener mItemClickListener = null;

    @NonNull
    @Override
    public DetailAlbumListAdapter.InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album, parent, false);
        return new InnerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailAlbumListAdapter.InnerHolder holder, int position) {
        //找到控件
        View view = holder.itemView;
        TextView id = view.findViewById(R.id.album_id);
        TextView title = view.findViewById(R.id.album_title);
        TextView playCount = view.findViewById(R.id.album_play_count);
        TextView playDuration = view.findViewById(R.id.album_play_duration);
        TextView time = view.findViewById(R.id.album_time);
        //设置数据
        Track track = mTracks.get(position);
        id.setText(String.valueOf(position + 1));
        title.setText(track.getTrackTitle());
        playCount.setText(track.getPlayCount() + "次");
        playDuration.setText(mTimeFormat.format(track.getDuration() * 1000));
        time.setText(mDatetimeFormat.format(track.getUpdatedAt()));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTracks.size();
    }

    public void setData(List<Track> tracks) {
        mTracks.clear();
        mTracks.addAll(tracks);
        notifyDataSetChanged();
    }

    public static class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void setItemClickListener(ItemClickListener listener) {
        mItemClickListener = listener;
    }

    public interface ItemClickListener {
        void onItemClick();
    }
}
