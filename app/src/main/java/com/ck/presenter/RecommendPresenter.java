package com.ck.presenter;

import com.ck.constant.RecommendConstant;
import com.ck.interfaces.IRecommendPresenter;
import com.ck.interfaces.IRecommendViewCallback;
import com.ck.util.L;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecommendPresenter implements IRecommendPresenter {

    private List<IRecommendViewCallback> mCallbacks = new ArrayList<>();

    private RecommendPresenter() {
    }

    private static RecommendPresenter sInstance = null;

    public static RecommendPresenter getInstance() {
        if (sInstance == null) {
            synchronized (RecommendPresenter.class) {
                if (sInstance == null) {
                    sInstance = new RecommendPresenter();
                }
            }
        }
        return sInstance;
    }

    /**
     * 获取推荐内容（猜你喜欢）
     */
    @Override
    public void getRecommendList() {
        updateLoading();
        getData();
    }

    /**
     * 喜马拉雅被墙
     */
    private void getData2() {
        List<Album> albumList = new ArrayList<>();
        for (int i = 0; i < RecommendConstant.RECOMMEND_COUNT; i++) {
            Album album = new Album();
            album.setAlbumTitle(i + "啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊");
            album.setAlbumIntro(i + "哈哈哈哈哈哈或或或或或哈哈哈哈哈哈啊哈哈");
            album.setPlayCount(i);
            album.setIncludeTrackCount(RecommendConstant.RECOMMEND_COUNT - i);
            album.setCoverUrlLarge("https://himg.bdimg.com/sys/portrait/item/pp.1.a6246271.hPNvzMm7d_71DIWDOusXCw?_t=1600244882165");
            albumList.add(album);
        }
        handleRecommendResult(albumList);
    }

    /**
     * 从喜马拉雅获得数据
     */
    private void getData() {
        Map<String, String> map = new HashMap<>();
        map.put(DTransferConstants.LIKE_COUNT, String.valueOf(RecommendConstant.RECOMMEND_COUNT));//LIKE_COUNT一页数据返回条数
        CommonRequest.getGuessLikeAlbum(map, new IDataCallBack<GussLikeAlbumList>() {
            @Override
            public void onSuccess(GussLikeAlbumList gussLikeAlbumList) {
                if (gussLikeAlbumList != null) {
                    List<Album> albumList = gussLikeAlbumList.getAlbumList();
                    for (Album album : albumList) {
                        L.d(album.toString());
                    }
                    handleRecommendResult(albumList);
                }
            }

            @Override
            public void onError(int i, String s) {
                L.e("errorCode-->" + i);
                L.e("errorMsg-->" + s);
                handleError();
            }
        });
    }

    /**
     * 发生错误
     */
    private void handleError() {
        if (mCallbacks != null) {
            for (IRecommendViewCallback callback : mCallbacks) {
                callback.networkError();
            }
        }
    }

    private void updateLoading() {
        for (IRecommendViewCallback callback : mCallbacks) {
            callback.onLoading();
        }
    }

    /**
     * 获得返回结果
     */
    private void handleRecommendResult(List<Album> albumList) {
//        albumList.clear();//测试数据为空的情况
        //通知UI更新页面
        if (albumList != null) {
            if (albumList.size() == 0) {//结果为空
                for (IRecommendViewCallback callback : mCallbacks) {
                    callback.onEmpty();
                }
            } else {
                for (IRecommendViewCallback callback : mCallbacks) {
                    callback.onRecommendListLoaded(albumList);
                }
            }
        }
    }

    /**
     * 注册view监听器
     */
    @Override
    public void registerViewCallback(IRecommendViewCallback callback) {
        if (mCallbacks != null && !mCallbacks.contains(callback)) {//contains包含
            mCallbacks.add(callback);
        }
    }

    /**
     * 注销view监听器
     */
    @Override
    public void unRegisterViewCallback(IRecommendViewCallback callback) {
        if (mCallbacks != null) {
            mCallbacks.remove(callback);
        }
    }
}
