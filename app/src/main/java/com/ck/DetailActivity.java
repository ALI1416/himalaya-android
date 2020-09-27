package com.ck;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ck.adapter.DetailAlbumListAdapter;
import com.ck.base.BaseActivity;
import com.ck.constant.RecommendConstant;
import com.ck.interfaces.IAlbumDetailViewCallback;
import com.ck.interfaces.IPlayerCallback;
import com.ck.presenter.AlbumDetailPresenter;
import com.ck.presenter.PlayerPresenter;
import com.ck.view.UILoader;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class DetailActivity extends BaseActivity implements IAlbumDetailViewCallback, IPlayerCallback {
    private static final String TAG = "DetailActivity";
    private final DetailActivity _this = this;
    private View mRootView;
    private LinearLayout mDetailPlay;
    private ImageView mDetailPlayBtn;
    private TextView mDetailPlayText;
    private PlayerPresenter mPlayerPresenter;
    private List<Track> mCurrentTracks;
    private static final int DEFAULT_PLAY_INDEX = 0;

    private void log(String msg) {
        Log.d(TAG, msg);
    }

    private void toast(String msg) {
        Toast.makeText(_this, msg, Toast.LENGTH_SHORT).show();
    }

    private ImageView mDetailCoverBg;
    private ImageView mDetailHeadBg;
    private TextView mDetailTitle;
    private TextView mDetailAuthor;
    private AlbumDetailPresenter mAlbumDetailPresenter;
    private long mCurrentId = -1;
    private int mCurrentPages = 1;//专辑列表，默认第一页
    private RecyclerView mDetailAlbumRecyclerView;
    private DetailAlbumListAdapter mDetailAlbumListAdapter;
    private FrameLayout mDetailContainer;
    private UILoader mUiLoader;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);//状态栏隐藏
        getWindow().setStatusBarColor(Color.TRANSPARENT);//设置状态栏颜色透明
        initView();
        mAlbumDetailPresenter = AlbumDetailPresenter.getInstance();//专辑详情
        mAlbumDetailPresenter.registerViewCallback(this);
        mPlayerPresenter = PlayerPresenter.getInstance();//播放器详情
        mPlayerPresenter.registerViewCallback(this);
        updatePlayStatus(mPlayerPresenter.isPlaying());//更新播放状态
        initEven();
    }


    private void initView() {
        mDetailCoverBg = findViewById(R.id.detail_cover_bg);
        mDetailHeadBg = findViewById(R.id.detail_head_bg);
        mDetailTitle = findViewById(R.id.detail_title);
        mDetailAuthor = findViewById(R.id.detail_author);
        mDetailContainer = findViewById(R.id.detail_container);
        mDetailPlay = findViewById(R.id.detail_play);
        mDetailPlayBtn = findViewById(R.id.detail_play_btn);
        mDetailPlayText = findViewById(R.id.detail_play_text);
        if (mUiLoader == null) {
            mUiLoader = new UILoader(this) {
                @Override
                protected View getSuccessView(ViewGroup container) {
                    return createSuccessView(container);
                }
            };
        }
        mDetailContainer.removeAllViews();
        mDetailContainer.addView(mUiLoader);
        mUiLoader.setOnReload(new UILoader.OnUILoadClickListener() {
            @Override
            public void onNetworkErrorClick() {
                if (mAlbumDetailPresenter != null) {
                    mAlbumDetailPresenter.getAlbumDetail(mCurrentId, mCurrentPages, RecommendConstant.COUNT_ALBUM);
                }
            }

            @Override
            public void onEmptyClick() {
                if (mAlbumDetailPresenter != null) {
                    mAlbumDetailPresenter.getAlbumDetail(mCurrentId, mCurrentPages, RecommendConstant.COUNT_ALBUM);
                }
            }
        });
    }

    private void initEven() {
        //点击播放/暂停
        mDetailPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayerPresenter != null) {
                    //判断是否有播放列表
                    if (mPlayerPresenter.hasPlayList()) {
                        handleHasPlayList();
                    } else {
                        handleNotPlayList();
                    }
                }
            }
        });
    }

    /**
     * 存在播放列表
     */
    private void handleHasPlayList() {
        if (mPlayerPresenter.isPlaying()) {
            mPlayerPresenter.pause();
        } else {
            mPlayerPresenter.play();
        }
    }

    /**
     * 不存在播放列表
     */
    private void handleNotPlayList() {
        mPlayerPresenter.setPlayList(mCurrentTracks, DEFAULT_PLAY_INDEX);
    }

    private View createSuccessView(ViewGroup container) {
        mRootView = LayoutInflater.from(this).inflate(R.layout.item_detail_list, container, false);
        mDetailAlbumRecyclerView = mRootView.findViewById(R.id.detail_album_recycle_view);
        //RecyclerView布局管理和适配器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mDetailAlbumRecyclerView.setLayoutManager(layoutManager);
        mDetailAlbumListAdapter = new DetailAlbumListAdapter();
        mDetailAlbumRecyclerView.setAdapter(mDetailAlbumListAdapter);
        mDetailAlbumListAdapter.setItemClickListener(new DetailAlbumListAdapter.ItemClickListener() {
            @Override
            public void onItemClick(List<Track> tracks, int position) {
                //设置播放器数据
                PlayerPresenter playerPresenter = PlayerPresenter.getInstance();
                playerPresenter.setPlayList(tracks, position);
                //页面跳转
                Intent intent = new Intent(_this, PlayerActivity.class);
                startActivity(intent);
            }
        });
        return mRootView;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAlbumDetailPresenter != null) {
            mAlbumDetailPresenter.unRegisterViewCallback(this);
        }
        if (mPlayerPresenter != null) {
            mPlayerPresenter.unRegisterViewCallback(this);
        }
    }

    /**
     * 更新播放状态
     */
    private void updatePlayStatus(boolean playing) {
        if (playing) {
            mDetailPlayBtn.setImageResource(R.drawable.selector_player_stop);
            mDetailPlayText.setText(R.string.detail_play);
        } else {
            mDetailPlayBtn.setImageResource(R.drawable.selector_player_play);
            mDetailPlayText.setText(R.string.detail_stop);
        }
    }

    //region IAlbumDetailViewCallback
    @Override
    public void onDetailListLoaded(List<Track> tracks) {
        mCurrentTracks = tracks;
        if (tracks == null || tracks.size() == 0) {
            mUiLoader.updateStatus(UILoader.UIStatus.EMPTY);
        } else {
            mUiLoader.updateStatus(UILoader.UIStatus.SUCCESS);
        }
        mDetailAlbumListAdapter.setData(tracks);
    }

    @Override
    public void onAlbumLoader(Album album) {
        mCurrentId = album.getId();
        if (mDetailTitle != null) {
            mDetailTitle.setText(album.getAlbumTitle());
        }
        if (mDetailAuthor != null) {
            mDetailAuthor.setText(album.getAnnouncer().getNickname());
        }
        if (mDetailCoverBg != null) {
            Glide
                    .with(this)
                    .load(album.getCoverUrlSmall())
                    //BlurTransformation模糊度0-25，默认25；缩小倍数，默认1
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(10, 1)))
                    .into(mDetailCoverBg);
        }
        if (mDetailHeadBg != null) {
            Glide
                    .with(this)
                    .load(album.getCoverUrlSmall())
                    .into(mDetailHeadBg);
        }
        if (mAlbumDetailPresenter != null) { //获取专辑详情
            mAlbumDetailPresenter.getAlbumDetail(album.getId(), mCurrentPages, RecommendConstant.COUNT_ALBUM);
        }
    }

    @Override
    public void onNetworkError(int i, String s) {
        mUiLoader.updateStatus(UILoader.UIStatus.NETWORK_ERROR);
    }

    @Override
    public void onEmpty() {
        mUiLoader.updateStatus(UILoader.UIStatus.EMPTY);
    }

    @Override
    public void onLoading() {
        mUiLoader.updateStatus(UILoader.UIStatus.LOADING);
    }
    //endregion IAlbumDetailViewCallback

    //region IPlayerCallback
    @Override
    public void onPlayerStart() {
        updatePlayStatus(true);
    }

    @Override
    public void onPlayPause() {
        updatePlayStatus(false);
    }

    @Override
    public void onPlayStop() {
        updatePlayStatus(false);
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
    public void onPlayOrderChange(boolean isOrder) {

    }

    @Override
    public void onProgressChange(int currentProgress, int total) {

    }

    @Override
    public void onAdLoading() {

    }

    @Override
    public void onAdFinished() {

    }

    @Override
    public void onTrackUpdate(Track track, int index) {

    }
    //endregion IPlayerCallback

}
