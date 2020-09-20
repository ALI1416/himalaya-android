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
import com.ck.presenter.AlbumDetailPresenter;
import com.ck.view.UILoader;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;


public class DetailActivity extends BaseActivity implements IAlbumDetailViewCallback {
    private static final String TAG = "DetailActivity";
    private final DetailActivity _this = this;

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
        mAlbumDetailPresenter = AlbumDetailPresenter.getInstance();
        mAlbumDetailPresenter.registerViewCallback(this);
    }

    private void initView() {
        mDetailCoverBg = findViewById(R.id.detail_cover_bg);
        mDetailHeadBg = findViewById(R.id.detail_head_bg);
        mDetailTitle = findViewById(R.id.detail_title);
        mDetailAuthor = findViewById(R.id.detail_author);
        mDetailContainer = findViewById(R.id.detail_container);
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

    private View createSuccessView(ViewGroup container) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_detail_list, container, false);
        mDetailAlbumRecyclerView = view.findViewById(R.id.detail_album_recycle_view);
        //RecyclerView布局管理和适配器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mDetailAlbumRecyclerView.setLayoutManager(layoutManager);
        mDetailAlbumListAdapter = new DetailAlbumListAdapter();
        mDetailAlbumRecyclerView.setAdapter(mDetailAlbumListAdapter);
        mDetailAlbumListAdapter.setItemClickListener(new DetailAlbumListAdapter.ItemClickListener() {
            @Override
            public void onItemClick() {
                Intent intent = new Intent(_this, PlayerActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onDetailListLoaded(List<Track> tracks) {
        if (tracks != null && tracks.size() != 0) {
            if (mUiLoader != null) {
                mUiLoader.updateStatus(UILoader.UIStatus.SUCCESS);//获取到了数据
            }
        } else {
            if (mUiLoader != null) {
                mUiLoader.updateStatus(UILoader.UIStatus.EMPTY);//数据为空
            }
        }
        mDetailAlbumListAdapter.setData(tracks);
    }

    @Override
    public void onAlbumLoader(Album album) {
        mCurrentId = album.getId();
        if (mUiLoader != null) {
            mUiLoader.updateStatus(UILoader.UIStatus.LOADING);//准备去获取专辑列表
        }
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

}
