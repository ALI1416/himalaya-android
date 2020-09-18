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
     * 注册UI的回调
     */
    void registerViewCallback(IRecommendViewCallback callback);

    /**
     * 注销UI的回调
     */
    void unRegisterViewCallback(IRecommendViewCallback callback);
}
