package com.ck;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;


public class DetailActivity extends BaseActivity implements IAlbumDetailViewCallback {

    private ImageView mDetailCoverBg;
    private ImageView mDetailHeadBg;
    private TextView mDetailTitle;
    private TextView mDetailAuthor;
    private AlbumDetailPresenter mAlbumDetailPresenter;
    private int mCurrentPages = 1;//专辑列表，默认第一页
    private RecyclerView mDetailAlbumRecyclerView;
    private DetailAlbumListAdapter mDetailAlbumListAdapter;

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
        mDetailAlbumRecyclerView = findViewById(R.id.detail_album_recycle_view);
        //RecyclerView布局管理和适配器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mDetailAlbumRecyclerView.setLayoutManager(layoutManager);
        mDetailAlbumListAdapter = new DetailAlbumListAdapter();
        mDetailAlbumRecyclerView.setAdapter(mDetailAlbumListAdapter);
    }

    @Override
    public void onDetailListLoaded(List<Track> tracks) {
        mDetailAlbumListAdapter.setData(tracks);
    }

    @Override
    public void onAlbumLoader(Album album) {
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
        //获取专辑详情
        mAlbumDetailPresenter.getAlbumDetail(album.getId(), mCurrentPages, RecommendConstant.COUNT_ALBUM);
    }
}
