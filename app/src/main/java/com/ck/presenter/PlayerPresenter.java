package com.ck.presenter;

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

    private List<IPlayerCallback> mCallbacks = new ArrayList<>();

    private final XmPlayerManager mPlayerManger;
    private Track mCurrentTrack;
    private int mCurrentIndex;

    private PlayerPresenter() {
        mPlayerManger = XmPlayerManager.getInstance(BaseApplication.getAppContext());
        mPlayerManger.addAdsStatusListener(this);
        mPlayerManger.addPlayerStatusListener(this);
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

    public void setPlayList(List<Track> list, int playIndex) {
        L.d(list.get(playIndex).toString());
        if (mPlayerManger != null) {
            isPlayerListLoaded = true;
            mPlayerManger.setPlayList(list, playIndex);
            mCurrentTrack = list.get(playIndex);
            mCurrentIndex = playIndex;
        } else {
            L.e("列表为空");
        }
    }

    @Override
    public void play() {
        if (isPlayerListLoaded) {
            mPlayerManger.play();
        }
    }

    @Override
    public void pause() {
        if (isPlayerListLoaded) {
            mPlayerManger.pause();
        }
    }

    @Override
    public void stop() {
        if (isPlayerListLoaded) {
            mPlayerManger.stop();
        }
    }

    @Override
    public void playPre() {
        if (mPlayerManger != null) {
            mPlayerManger.playPre();
        }
    }

    @Override
    public void playNext() {
        if (mPlayerManger != null) {
            mPlayerManger.playNext();
        }
    }

    @Override
    public void switchPlayMode(XmPlayListControl.PlayMode mode) {

    }

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
     * 切换播放
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

    @Override
    public boolean isPlay() {
        return mPlayerManger.isPlaying();
    }

    @Override
    public void registerViewCallback(IPlayerCallback iPlayerCallback) {
        iPlayerCallback.onTrackUpdate(mCurrentTrack, mCurrentIndex);
        if (!mCallbacks.contains(iPlayerCallback)) {
            mCallbacks.add(iPlayerCallback);
        }
    }

    @Override
    public void unRegisterViewCallback(IPlayerCallback iPlayerCallback) {
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
