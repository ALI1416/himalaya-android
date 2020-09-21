package com.ck.presenter;

import com.ck.base.BaseApplication;
import com.ck.interfaces.IPlayerCallback;
import com.ck.interfaces.IPlayerPresenter;
import com.ck.util.L;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.util.List;

public class PlayerPresenter implements IPlayerPresenter {

    private final XmPlayerManager mPlayerManger;

    private PlayerPresenter() {
        mPlayerManger = XmPlayerManager.getInstance(BaseApplication.getAppContext());
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

    }

    @Override
    public void stop() {

    }

    @Override
    public void playPre() {

    }

    @Override
    public void playNext() {

    }

    @Override
    public void switchPlayMode(XmPlayListControl.PlayMode mode) {

    }

    @Override
    public void getPlayList() {

    }

    @Override
    public void playByIndex(int index) {

    }

    @Override
    public void seekTo(int progress) {

    }

    @Override
    public void registerViewCallback(IPlayerCallback iPlayerCallback) {

    }

    @Override
    public void unRegisterViewCallback(IPlayerCallback iPlayerCallback) {

    }
}
