package com.ck;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ck.adapter.PlayerViewPagerAdapter;
import com.ck.base.BaseActivity;
import com.ck.interfaces.IPlayerCallback;
import com.ck.presenter.PlayerPresenter;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.text.SimpleDateFormat;
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
    private SimpleDateFormat mMmssFormat = new SimpleDateFormat("mm:ss");
    private SimpleDateFormat mHhmmssFormat = new SimpleDateFormat("hh:mm:ss");
    private int mCurrentProgress = 0;//当前播放进度
    private boolean mIsUserTouchSeekBar = false;//是否用户触摸进度条
    private boolean mIsUserSlidPager = false;//是否用户滑动图片
    private String mTitleText;
    private PlayerViewPagerAdapter mPlayerViewPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        mPlayerPresenter = PlayerPresenter.getInstance();
        mPlayerPresenter.registerViewCallback(this);
        initView();
        mPlayerPresenter.getPlayList();
        initEven();
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
        if (!TextUtils.isEmpty(mTitleText)) {
            mTitle.setText(mTitleText);
        }
        //设置适配器
        mPlayerViewPagerAdapter = new PlayerViewPagerAdapter();
        mViewPager.setAdapter(mPlayerViewPagerAdapter);

    }

    //region initEven.start
    @SuppressLint("ClickableViewAccessibility")
    private void initEven() {
        //播放按钮
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
        //拖动进度条
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {//进度条改变 fromUser是否用户触摸
                if (fromUser) {
                    mCurrentProgress = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {//开始触摸
                mIsUserTouchSeekBar = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {//触摸结束
                mIsUserTouchSeekBar = false;
                mPlayerPresenter.seekTo(mCurrentProgress);//设置当前进度
            }
        });
        //上一首
        mPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayerPresenter != null) {
                    mPlayerPresenter.playPre();
                }
            }
        });
        //下一首
        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayerPresenter != null) {
                    mPlayerPresenter.playNext();
                }
            }
        });
        //图片切换
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mPlayerPresenter != null && mIsUserSlidPager) {
                    mPlayerPresenter.playByIndex(position);
                }
                mIsUserSlidPager = false;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN: {
                        mIsUserSlidPager = true;
                        break;
                    }
                    default: {

                    }
                }
                return false;
            }
        });
    }
    //endregion initEven.end

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayerPresenter != null) {
            mPlayerPresenter.unRegisterViewCallback(this);
            mPlayerPresenter = null;
        }
    }

    //region IPlayerCallback.start

    /**
     * 开始播放
     */
    @Override
    public void onPlayerStart() {
        if (mPlay != null) {
            mPlay.setImageResource(R.drawable.selector_player_stop);
        }
    }

    /**
     * 暂停播放
     */
    @Override
    public void onPlayPause() {
        if (mPlay != null) {
            mPlay.setImageResource(R.drawable.selector_player_play);
        }
    }

    @Override
    public void onPlayStop() {
        if (mPlay != null) {
            mPlay.setImageResource(R.drawable.selector_player_stop);
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
        if (mPlayerViewPagerAdapter != null) {
            mPlayerViewPagerAdapter.setData(list);
        }

    }

    @Override
    public void onPlayModeChange(XmPlayListControl.PlayMode mode) {

    }

    @Override
    public void onProgressChange(int currentProgress, int total) {
        mSeekBar.setMax(total);
        //更新进度条时间
        String totalDuration;
        if (total > 1000 * 60 * 60) {
            totalDuration = mMmssFormat.format(total);
        } else {
            totalDuration = mHhmmssFormat.format(total);
        }
        if (mSeekTotal != null) {
            mSeekTotal.setText(totalDuration);
        }
        String currentPosition;
        if (currentProgress > 1000 * 60 * 60) {
            currentPosition = mMmssFormat.format(currentProgress);
        } else {
            currentPosition = mHhmmssFormat.format(currentProgress);
        }
        if (mSeekNow != null) {
            mSeekNow.setText(currentPosition);
        }
        //更新进度条
        if (mSeekBar != null && !mIsUserTouchSeekBar) {
            mSeekBar.setProgress(currentProgress);
        }
    }

    @Override
    public void onAdLoading() {

    }

    @Override
    public void onAdFinished() {

    }

    @Override
    public void onTrackUpdate(Track track, int index) {
        mTitleText = track.getTrackTitle();
        if (mTitle != null) {//改变标题
            mTitle.setText(mTitleText);
        }
        if (mViewPager != null) {
            mViewPager.setCurrentItem(index);
        }
    }

    //endregion IPlayerCallback.end


}
