package com.ck.interfaces;

import com.ck.base.IBasePresenter;

public interface IAlbumDetailPresenter extends IBasePresenter<IAlbumDetailViewCallback> {
    /**
     * 获取专辑详情
     */
    void getAlbumDetail(long albumId, int pages, int rows);

    /**
     * 下拉刷新
     */
    void pullRefresh();

    /**
     * 上拉加载更多
     */
    void loadMore();
}
