package com.ck.base;

public interface IBasePresenter<V> {
    /**
     * 注册UI的回调
     */
    void registerViewCallback(V v);

    /**
     * 注销UI的回调
     */
    void unRegisterViewCallback(V v);
}
