package com.ck.interfaces;

import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.util.List;

public interface IPlayerCallback {

    /**
     * 开始播放
     */
    void onPlayerStart();

    /**
     * 暂停播放
     */
    void onPlayPause();

    /**
     * 停止播放
     */
    void onPlayStop();

    /**
     * 播放错误
     */
    void onPlayError();

    /**
     * 播放下一首
     */
    void nextPlay(Track track);

    /**
     * 播放上一首
     */
    void prePlay(Track track);

    /**
     * 播放列表数据加载完成
     */
    void onListLoaded(List<Track> list);

    /**
     * 播放模式改变
     */
    void onPlayModeChange(XmPlayListControl.PlayMode mode);

    /**
     * 进度条改变
     */
    void onProgressChange(int currentProgress, int total);

    /**
     * 公告正在加载
     */
    void onAdLoading();

    /**
     * 公告结束
     */
    void onAdFinished();

    /**
     * track更新
     */
    void onTrackUpdate(Track track, int index);

}
