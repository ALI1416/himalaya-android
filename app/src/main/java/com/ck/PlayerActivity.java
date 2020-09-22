package com.ck;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.ck.base.BaseActivity;
import com.ck.interfaces.IPlayerCallback;
import com.ck.presenter.PlayerPresenter;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.util.List;

public class PlayerActivity extends BaseActivity implements IPlayerCallback {

    private TextView mTitle;
    private ViewPager mViewPager;
    private TextView mSeekNow;
    private TextView mSeekTotal;
    private SeekBar mSeekBar;
    private ImageView mMode;
    private ImageView mList;
    private ImageView mPlay;
    private ImageView mPre;
    private ImageView mNext;
    private PlayerPresenter mPlayerPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        mPlayerPresenter = PlayerPresenter.getInstance();
        mPlayerPresenter.registerViewCallback(this);
        initView();
        initEven();
        startPlay();
    }

    /**
     * 开始播放
     */
    private void startPlay() {
        if (mPlayerPresenter != null) {
            mPlayerPresenter.play();
        }
    }

    private void initView() {
        mTitle = findViewById(R.id.player_title);
        mViewPager = findViewById(R.id.player_view_pager);
        mSeekNow = findViewById(R.id.play_seek_now);
        mSeekTotal = findViewById(R.id.player_seek_total);
        mSeekBar = findViewById(R.id.play_seek_bar);
        mMode = findViewById(R.id.player_mode);
        mList = findViewById(R.id.player_list);
        mPlay = findViewById(R.id.player_play);
        mPre = findViewById(R.id.player_pre);
        mNext = findViewById(R.id.player_next);
    }

    private void initEven() {
        mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayerPresenter.isPlay()) {
                    mPlayerPresenter.pause();
                } else {
                    mPlayerPresenter.play();
                }
            }
        });
    }

//=============IPlayerCallback.start================

    /**
     * 开始播放
     */
    @Override
    public void onPlayerStart() {
        if (mPlay != null) {
            mPlay.setImageResource(R.mipmap.stop_normal);
        }
    }

    /**
     * 暂停播放
     */
    @Override
    public void onPlayPause() {
        if (mPlay != null) {
            mPlay.setImageResource(R.mipmap.play_normal);
        }
    }

    @Override
    public void onPlayStop() {
        if (mPlay != null) {
            mPlay.setImageResource(R.mipmap.stop_normal);
        }
    }

    @Override
    public void onPlayError() {

    }

    @Override
    public void nextPlay(Track track) {

    }

    @Override
    public void prePlay(Track track) {

    }

    @Override
    public void onListLoaded(List<Track> list) {

    }

    @Override
    public void onPlayModeChange(XmPlayListControl.PlayMode mode) {

    }

    @Override
    public void onProgressChange(long currentProgress, long total) {

    }

    @Override
    public void onAdLoading() {

    }

    @Override
    public void onAdFinished() {

    }

    //=============IPlayerCallback.end================

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayerPresenter != null) {
            mPlayerPresenter.unRegisterViewCallback(this);
            mPlayerPresenter = null;
        }
    }
}
