package com.ck.interfaces;

/**
 * Recommend逻辑层接口
 */
public interface IRecommendPresenter {
    /**
     * 获取推荐列表
     */
    void getRecommendList();

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
     *
     * @param callback
     */
    void registerViewCallback(IRecommendViewCallback callback);

    /**
     * 注销UI的回调
     *
     * @param callback
     */
    void unRegisterViewCallback(IRecommendViewCallback callback);
}
