package com.ck.interfaces;

import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

/**
 * 专辑回调
 */
public interface IAlbumDetailViewCallback {
    /**
     * 加载到了专辑内容
     */
    void onDetailListLoaded(List<Track> tracks);

    /**
     * 加载到了一个的详情
     */
    void onAlbumLoader(Album album);

    /**
     * 网络异常
     */
    void onNetworkError(int i, String s);

    /**
     * 数据为空
     */
    void onEmpty();

    /**
     * 正在加载
     */
    void onLoading();
}
