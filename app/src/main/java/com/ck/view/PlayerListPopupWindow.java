package com.ck.view;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ck.R;
import com.ck.adapter.PlayerPopupListAdapter;
import com.ck.base.BaseApplication;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.util.List;

public class PlayerListPopupWindow extends PopupWindow {

    private final View mView;
    private TextView mClose;
    private RecyclerView mRecyclerView;
    private PlayerPopupListAdapter mAdapter;
    private ImageView mSwitchPlayModeBtn;
    private ImageView mSwitchPlayOrderBtn;
    private TextView mSwitchPlayModeText;
    private TextView mSwitchPlayOrderText;
    private PlayListActionListener mPlayModeClickListener = null;

    public PlayerListPopupWindow() {
        //设置宽高
        super(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置外部点击后关闭弹窗，一定要先设置setBackgroundDrawable
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//这个是圆角填充的颜色，不是外部的颜色
        setOutsideTouchable(true);
        //设置动画
        setAnimationStyle(R.style.popup_animation);
        //设置view
        mView = LayoutInflater.from(BaseApplication.getAppContext()).inflate(R.layout.popup_player_list, null);
        setContentView(mView);
        initView();
        initEven();
    }

    private void initView() {
        mClose = mView.findViewById(R.id.player_list_close);
        mRecyclerView = mView.findViewById(R.id.player_list_recycle_view);
        mSwitchPlayModeBtn = mView.findViewById(R.id.popup_switch_play_mode_btn);
        mSwitchPlayModeText = mView.findViewById(R.id.popup_switch_play_mode_text);
        mSwitchPlayOrderBtn = mView.findViewById(R.id.popup_switch_play_order_btn);
        mSwitchPlayOrderText = mView.findViewById(R.id.popup_switch_play_order_text);
        //设置布局
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BaseApplication.getAppContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //设置适配器
        mAdapter = new PlayerPopupListAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initEven() {
        //关闭
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayerListPopupWindow.this.dismiss();
            }
        });
        //切换播放模式
        mSwitchPlayModeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayModeClickListener != null) {
                    mPlayModeClickListener.onPlayModeClick();
                }
            }
        });
        //切换排序
        mSwitchPlayOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayModeClickListener != null) {
                    mPlayModeClickListener.onPlayOrderClick();
                }
            }
        });
    }

    /**
     * 给适配器设置数据
     */
    public void setListData(List<Track> data) {
        if (mAdapter != null) {
            mAdapter.setData(data);
        }
    }

    /**
     * 设置当前播放的节目
     */
    public void setCurrentPlayPosition(int position) {
        if (mAdapter != null) {
            mAdapter.setCurrentPlayPosition(position);
            mRecyclerView.scrollToPosition(position);//设置当前播放的移动到可视范围内
        }
    }

    /**
     * 切换播放模式时改变图标和文字
     */
    public void updatePlayMode(XmPlayListControl.PlayMode currentPlayMode) {
        int resId = R.drawable.selector_player_mode_list;
        int textId = R.string.play_mode_list_text;
        switch (currentPlayMode) {
            case PLAY_MODEL_LIST: {
                resId = R.drawable.selector_player_mode_list;
                textId = R.string.play_mode_list_text;
                break;
            }
            case PLAY_MODEL_LIST_LOOP: {
                resId = R.drawable.selector_player_mode_list_loop;
                textId = R.string.play_mode_list_loop_text;
                break;
            }
            case PLAY_MODEL_RANDOM: {
                resId = R.drawable.selector_player_mode_random;
                textId = R.string.play_mode_random_text;
                break;
            }
            case PLAY_MODEL_SINGLE_LOOP: {
                resId = R.drawable.selector_player_mode_single_loop;
                textId = R.string.play_mode_single_loop_text;
                break;
            }
        }
        mSwitchPlayModeBtn.setImageResource(resId);
        mSwitchPlayModeText.setText(textId);
    }

    /**
     * 切换播放顺序
     */
    public void updatePlayOrder(boolean isOrder) {
        if (isOrder) {
            mSwitchPlayOrderBtn.setImageResource(R.drawable.selector_player_list_desc);
            mSwitchPlayOrderText.setText(R.string.play_order_desc_text);
        } else {
            mSwitchPlayOrderBtn.setImageResource(R.drawable.selector_player_list_asc);
            mSwitchPlayOrderText.setText(R.string.play_order_asc_text);
        }
    }

    public void setPlayListItemClickListener(PlayListItemClickListener listener) {
        if (mAdapter != null) {
            mAdapter.setOnItemClickListener(listener);
        }
    }

    public interface PlayListItemClickListener {
        void onItemClick(int position);
    }

    public void setPlayListActionListener(PlayListActionListener listener) {
        mPlayModeClickListener = listener;
    }

    public interface PlayListActionListener {
        void onPlayModeClick();

        void onPlayOrderClick();
    }
}
