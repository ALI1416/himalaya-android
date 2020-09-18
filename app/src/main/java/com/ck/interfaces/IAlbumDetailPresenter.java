package com.ck.interfaces;

public interface IAlbumDetailPresenter {
    /**
     * 获取专辑详情
     */
    void getAlbumDetail(int albumId, int pages, int rows);

    /**
     * 下拉刷新
     */
    void pullRefresh();

    /**
     * 上拉加载更多
     */
    void loadMore();

    /**
     * 注册UI的回调
     */
    void registerViewCallback(IAlbumDetailViewCallback callback);

    /**
     * 注销UI的回调
     */
    void unRegisterViewCallback(IAlbumDetailViewCallback callback);
}
