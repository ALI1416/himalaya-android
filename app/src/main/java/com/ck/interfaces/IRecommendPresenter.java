package com.ck.interfaces;

import com.ck.base.IBasePresenter;

/**
 * Recommend逻辑层接口
 */
public interface IRecommendPresenter extends IBasePresenter<IRecommendViewCallback> {
    /**
     * 获取推荐列表
     */
    void getRecommendList();
}
