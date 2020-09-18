package com.ck.presenter;

import com.ck.interfaces.IAlbumDetailPresenter;
import com.ck.interfaces.IAlbumDetailViewCallback;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.ArrayList;
import java.util.List;

public class AlbumDetailPresenter implements IAlbumDetailPresenter {

    private Album mTargetAlbum;
    private List<IAlbumDetailViewCallback> mCallbacks = new ArrayList<>();

    private AlbumDetailPresenter() {
    }

    private static AlbumDetailPresenter sInstance = null;

    public static AlbumDetailPresenter getInstance() {
        if (sInstance == null) {
            synchronized (AlbumDetailPresenter.class) {
                if (sInstance == null) {
                    sInstance = new AlbumDetailPresenter();
                }
            }
        }
        return sInstance;
    }

    @Override
    public void getAlbumDetail(int albumId, int pages, int rows) {

    }

    @Override
    public void pullRefresh() {

    }

    @Override
    public void loadMore() {

    }

    /**
     * 注册view监听器
     */
    @Override
    public void registerViewCallback(IAlbumDetailViewCallback callback) {
        if (mCallbacks != null && !mCallbacks.contains(callback)) {
            mCallbacks.add(callback);
        }
    }

    /**
     * 注销view监听器
     */
    @Override
    public void unRegisterViewCallback(IAlbumDetailViewCallback callback) {
        if (mCallbacks != null) {
            mCallbacks.remove(callback);
        }
    }

    /**
     * 设置专辑内容
     * @param targetAlbum
     */
    public void setTargetAlbum(Album targetAlbum) {
        mTargetAlbum = targetAlbum;
    }
}
