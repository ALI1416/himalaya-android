package com.ck;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.ck.base.BaseActivity;
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
    }

    @Override
    public void onDetailListLoaded(List<Track> tracks) {

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
    }
}
