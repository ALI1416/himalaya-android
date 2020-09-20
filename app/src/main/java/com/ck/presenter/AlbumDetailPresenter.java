package com.ck.presenter;

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
        getData2(albumId, pages, rows);
    }

    private void getData(long albumId, int pages, int rows) {
        Map<String, String> map = new HashMap<>();
        map.put(DTransferConstants.ALBUM_ID, String.valueOf(albumId));
        map.put(DTransferConstants.SORT, "asc");
        map.put(DTransferConstants.PAGE, String.valueOf(pages));
        map.put(DTransferConstants.PAGE_SIZE, String.valueOf(rows));
        CommonRequest.getTracks(map, new IDataCallBack<TrackList>() {
            @Override
            public void onSuccess(TrackList trackList) {
                List<Track> tracks = trackList.getTracks();
                for (Track track : tracks) {
                    L.d(track.toString());
                }
                handleAlbumDetailResult(tracks);
            }

            @Override
            public void onError(int i, String s) {
                L.e("errorCode-->" + i);
                L.e("errorMsg-->" + s);
            }
        });
    }

    private void getData2(long albumId, int pages, int rows) {
        List<Track> tracks = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            Track track = new Track();
            tracks.add(track);
        }
        handleAlbumDetailResult(tracks);
    }

    /**
     * 更新专辑列表详情
     */
    private void handleAlbumDetailResult(List<Track> tracks) {
        for (IAlbumDetailViewCallback mCallback : mCallbacks) {
            mCallback.onDetailListLoaded(tracks);
        }
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
     *
     * @param targetAlbum
     */
    public void setTargetAlbum(Album targetAlbum) {
        mTargetAlbum = targetAlbum;
    }
}
