package com.ck.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.ck.base.BaseApplication;
import com.ck.interfaces.IPlayerCallback;
import com.ck.interfaces.IPlayerPresenter;
import com.ck.util.L;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.advertis.Advertis;
import com.ximalaya.ting.android.opensdk.model.advertis.AdvertisList;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.advertis.IXmAdsStatusListener;
import com.ximalaya.ting.android.opensdk.player.constants.PlayerConstants;
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;

import java.util.ArrayList;
import java.util.List;

public class PlayerPresenter implements IPlayerPresenter, IXmAdsStatusListener, IXmPlayerStatusListener {

    private static final String PLAY_MODE_SP_NAME = "PlayMode";
    private static final String PLAY_MODE_SP_KEY = "currentPlayMode";

    private List<IPlayerCallback> mCallbacks = new ArrayList<>();

    private final XmPlayerManager mPlayerManger;
    private Track mCurrentTrack;
    private int mCurrentIndex;
    private final SharedPreferences mPlayMode;
    private XmPlayListControl.PlayMode mCurrentPlayMode = XmPlayListControl.PlayMode.PLAY_MODEL_LIST;
    private XmPlayListControl.PlayMode mDefaultPlayMode = XmPlayListControl.PlayMode.PLAY_MODEL_LIST;

    private PlayerPresenter() {
        mPlayerManger = XmPlayerManager.getInstance(BaseApplication.getAppContext());
        mPlayerManger.addAdsStatusListener(this);
        mPlayerManger.addPlayerStatusListener(this);
        //记录播放模式
        mPlayMode = BaseApplication.getAppContext().getSharedPreferences(PLAY_MODE_SP_NAME, Context.MODE_PRIVATE);
    }

    private static PlayerPresenter sInstance = null;
    private boolean isPlayerListLoaded = false;

    public static PlayerPresenter getInstance() {
        if (sInstance == null) {
            synchronized (PlayerPresenter.class) {
                if (sInstance == null) {
                    sInstance = new PlayerPresenter();
                }
            }
        }
        return sInstance;
    }

    /**
     * 设置播放列表
     */
    public void setPlayList(List<Track> list, int playIndex) {
        if (mPlayerManger != null) {
            isPlayerListLoaded = true;
            mPlayerManger.setPlayList(list, playIndex);
            mCurrentTrack = list.get(playIndex);
            mCurrentIndex = playIndex;
        } else {
            L.e("列表为空");
        }
    }

    /**
     * 播放
     */
    @Override
    public void play() {
        if (isPlayerListLoaded) {
            mPlayerManger.play();
        }
    }

    /**
     * 暂停
     */
    @Override
    public void pause() {
        if (isPlayerListLoaded) {
            mPlayerManger.pause();
        }
    }

    /**
     * 停止
     */
    @Override
    public void stop() {
        if (isPlayerListLoaded) {
            mPlayerManger.stop();
        }
    }

    /**
     * 播放上一首
     */
    @Override
    public void playPre() {
        if (mPlayerManger != null) {
            mPlayerManger.playPre();
        }
    }

    /**
     * 播放下一首
     */
    @Override
    public void playNext() {
        if (mPlayerManger != null) {
            mPlayerManger.playNext();
        }
    }

    /**
     * 切换播放模式
     */
    @Override
    public void switchPlayMode(XmPlayListControl.PlayMode mode) {
        mCurrentPlayMode = mode;
        if (mPlayerManger != null) {
            mPlayerManger.setPlayMode(mode);
            for (IPlayerCallback callback : mCallbacks) {//通知UI更新
                callback.onPlayModeChange(mode);
            }
        }
    }

    /**
     * 获取播放列表
     */
    @Override
    public void getPlayList() {
        if (mPlayerManger != null) {
            List<Track> playList = mPlayerManger.getPlayList();
            for (IPlayerCallback callback : mCallbacks) {
                callback.onListLoaded(playList);
            }
        }
    }

    /**
     * 切歌
     */
    @Override
    public void playByIndex(int index) {
        if (mPlayerManger != null) {
            mPlayerManger.play(index);
        }
    }

    /**
     * 更新进度条进度
     */
    @Override
    public void seekTo(int progress) {
        mPlayerManger.seekTo(progress);
    }

    /**
     * 判断是否正在播放
     */
    @Override
    public boolean isPlay() {
        return mPlayerManger.isPlaying();
    }

    @Override
    public void registerViewCallback(IPlayerCallback iPlayerCallback) {
        int mode = mPlayMode.getInt(PLAY_MODE_SP_KEY, mDefaultPlayMode.ordinal());//获取储存的播放模式
        mCurrentPlayMode = XmPlayListControl.PlayMode.values()[mode];//values()[]把int类型转为枚举类型
        iPlayerCallback.onPlayModeChange(mCurrentPlayMode);//切换播放模式
        iPlayerCallback.onTrackUpdate(mCurrentTrack, mCurrentIndex);
        if (!mCallbacks.contains(iPlayerCallback)) {
            mCallbacks.add(iPlayerCallback);
        }
    }

    @Override
    public void unRegisterViewCallback(IPlayerCallback iPlayerCallback) {
        SharedPreferences.Editor edit = mPlayMode.edit();
        edit.putInt(PLAY_MODE_SP_KEY, mCurrentPlayMode.ordinal());//ordinal()把枚举类型转为int类型
        edit.apply();//写入播放模式状态
        mCallbacks.remove(iPlayerCallback);
    }

    //region 广告回调开始

    @Override
    public void onStartGetAdsInfo() {

    }

    @Override
    public void onGetAdsInfo(AdvertisList advertisList) {

    }

    @Override
    public void onAdsStartBuffering() {

    }

    @Override
    public void onAdsStopBuffering() {

    }

    @Override
    public void onStartPlayAds(Advertis advertis, int i) {

    }

    @Override
    public void onCompletePlayAds() {

    }

    @Override
    public void onError(int i, int i1) {

    }

    //endregion 广告回调结束

    //region 播放器回调开始

    @Override
    public void onPlayStart() {
        for (IPlayerCallback callback : mCallbacks) {
            callback.onPlayerStart();
        }
    }

    @Override
    public void onPlayPause() {
        for (IPlayerCallback callback : mCallbacks) {
            callback.onPlayStop();
        }
    }

    @Override
    public void onPlayStop() {
        for (IPlayerCallback callback : mCallbacks) {
            callback.onPlayStop();
        }
    }

    @Override
    public void onSoundPlayComplete() {

    }

    /**
     * 播放器准备好了，去播放
     */
    @Override
    public void onSoundPrepared() {
        if (mPlayerManger.getPlayerStatus() == PlayerConstants.STATE_PREPARED) {
            mPlayerManger.play();
        }
    }

    /**
     * 切歌
     */
    @Override
    public void onSoundSwitch(PlayableModel lastModel, PlayableModel currentMode) {
        mCurrentIndex = mPlayerManger.getCurrentIndex();
        if (currentMode instanceof Track) {
            Track currentTrack = (Track) currentMode;
            for (IPlayerCallback callback : mCallbacks) {
                callback.onTrackUpdate(currentTrack, mCurrentIndex);
            }
        }
    }

    @Override
    public void onBufferingStart() {

    }

    @Override
    public void onBufferingStop() {

    }

    @Override
    public void onBufferProgress(int i) {

    }

    /**
     * 播放进度 单位：毫秒
     */
    @Override
    public void onPlayProgress(int currentPosition, int duration) {
        for (IPlayerCallback callback : mCallbacks) {
            callback.onProgressChange(currentPosition, duration);
        }
    }

    @Override
    public boolean onError(XmPlayerException e) {
        return false;
    }

    //endregion 播放器回调结束

}
