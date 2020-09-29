package com.ck.presenter;

import com.ck.constant.RecommendConstant;
import com.ck.interfaces.IAlbumDetailPresenter;
import com.ck.interfaces.IAlbumDetailViewCallback;
import com.ck.util.L;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.model.track.TrackList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlbumDetailPresenter implements IAlbumDetailPresenter {

    private Album mTargetAlbum;
    private List<IAlbumDetailViewCallback> mCallbacks = new ArrayList<>();
    private List<Track> mTracks = new ArrayList<>();
    private long mCurrentAlbumId = -1;
    private int mCurrentPages = 1;

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
    public void getAlbumDetail(long albumId, int pages, int rows) {
        mCurrentAlbumId = albumId;
        mCurrentPages = pages;
        mTracks.clear();
        updateLoading();
        doLoaded2(true);
        // getData2(albumId, pages, rows);
    }

    private void doLoaded(final boolean isRefresh) {
        Map<String, String> map = new HashMap<>();
        map.put(DTransferConstants.ALBUM_ID, String.valueOf(mCurrentAlbumId));
        map.put(DTransferConstants.SORT, "asc");
        map.put(DTransferConstants.PAGE, String.valueOf(mCurrentPages));
        map.put(DTransferConstants.PAGE_SIZE, String.valueOf(RecommendConstant.COUNT_ALBUM));
        CommonRequest.getTracks(map, new IDataCallBack<TrackList>() {
            @Override
            public void onSuccess(TrackList trackList) {
                List<Track> tracks = trackList.getTracks();
                for (Track track : mTracks) {
                    L.d(track.toString());
                }
                if (isRefresh) {
                    mTracks.clear();
                    mCurrentPages = 1;
                } else {
                    mCurrentPages++;
                }
                mTracks.addAll(tracks);
                handleAlbumDetailResult(mTracks);
            }

            @Override
            public void onError(int i, String s) {
                L.e("errorCode-->" + i);
                L.e("errorMsg-->" + s);
                handleError(i, s);
            }
        });
    }

    private void doLoaded2(final boolean isRefresh) {
        List<Track> tracks = new ArrayList<>();
        for (int i = 0; i < RecommendConstant.COUNT_ALBUM; i++) {
            Track track = new Track();
            track.setTrackTitle(i + "标题标题标题标题标题标题标题标题标题标题标题标题");
            track.setPlayCount(i);
            track.setDuration(100 * i);
            track.setUpdatedAt(1000000000L * i);
            track.setCoverUrlLarge("https://himg.bdimg.com/sys/portrait/item/pp.1.a6246271.hPNvzMm7d_71DIWDOusXCw?_t=1600244882165");
            tracks.add(track);
        }
        if (isRefresh) {
            mTracks.clear();
            mCurrentPages = 1;
        } else {
            mCurrentPages++;
        }
        mTracks.addAll(tracks);
        handleAlbumDetailResult(mTracks);
    }

    /**
     * 网络错误
     */
    private void handleError(int i, String s) {
        for (IAlbumDetailViewCallback callback : mCallbacks) {
            callback.onNetworkError(i, s);
        }
    }

    private void updateLoading() {
        for (IAlbumDetailViewCallback callback : mCallbacks) {
            callback.onLoading();
        }
    }

    /**
     * 更新专辑列表详情
     */
    private void handleAlbumDetailResult(List<Track> trackList) {
        if (trackList != null) {
            if (trackList.size() == 0) {
                for (IAlbumDetailViewCallback callback : mCallbacks) {
                    callback.onEmpty();
                }
            } else {
                for (IAlbumDetailViewCallback callback : mCallbacks) {
                    callback.onDetailListLoaded(trackList);
                }
            }
        }
    }

    @Override
    public void pullRefresh() {
        doLoaded2(true);
    }

    @Override
    public void loadMore() {
        doLoaded2(false);
    }

    /**
     * 注册view监听器
     */
    @Override
    public void registerViewCallback(IAlbumDetailViewCallback callback) {
        if (mCallbacks != null && !mCallbacks.contains(callback)) {
            mCallbacks.add(callback);
            if (mTargetAlbum != null) {
                callback.onAlbumLoader(mTargetAlbum);
            }
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
     */
    public void setTargetAlbum(Album targetAlbum) {
        mTargetAlbum = targetAlbum;
    }
}
