package com.ck.interfaces;

import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

/**
 * 专辑回调
 */
public interface IAlbumDetailViewCallback {
    /**
     * 加载到了专辑内容
     *
     * @param tracks
     */
    void onDetailListLoaded(List<Track> tracks);
}
