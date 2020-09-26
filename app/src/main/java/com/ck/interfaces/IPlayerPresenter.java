package com.ck.interfaces;

import com.ck.base.IBasePresenter;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

public interface IPlayerPresenter extends IBasePresenter<IPlayerCallback> {
    /**
     * 播放
     */
    void play();

    /**
     * 暂停
     */
    void pause();

    /**
     * 停止
     */
    void stop();

    /**
     * 播放上一首
     */
    void playPre();

    /**
     * 播放下一首
     */
    void playNext();

    /**
     * 切换播放模式
     */
    void switchPlayMode(XmPlayListControl.PlayMode mode);

    /**
     * 获取播放列表
     */
    void getPlayList();

    /**
     * 根据节目位置进行播放
     */
    void playByIndex(int index);

    /**
     * 切换播放进度
     */
    void seekTo(int progress);

    /**
     * 判断是否正在播放
     */
    boolean isPlay();

    /**
     * 切换播放列表顺序
     */
    void switchPlayList(boolean isOrder);
}
