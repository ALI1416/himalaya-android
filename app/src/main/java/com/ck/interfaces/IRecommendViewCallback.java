package com.ck.interfaces;

import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;

/**
 * Recommend界面回调接口
 */
public interface IRecommendViewCallback {
    /**
     * 获取到了推荐内容
     *
     * @param result
     */
    void onRecommendListLoaded(List<Album> result);

    /**
     * 获取到了加载更多的内容
     *
     * @param result
     */
    void onLoadedMore(List<Album> result);

    /**
     * 获取到了刷新后的内容
     *
     * @param result
     */
    void onRefresh(List<Album> result);
}
