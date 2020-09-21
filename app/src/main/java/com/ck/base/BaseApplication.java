package com.ck.base;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;

public class BaseApplication extends Application {

    private static Handler sHandler = null;
    private static Context sContext = null;

    @Override
    public void onCreate() {
        super.onCreate();
        /*设置AppKey等*/
        CommonRequest mXimalaya = CommonRequest.getInstanse();
        if (DTransferConstants.isRelease) {
            String mAppSecret = "afe063d2e6df361bc9f1fb8bb8210d67";
            mXimalaya.setAppkey("af1d317b871e0e7e2ce45872caa34d9a");
            mXimalaya.setPackid("com.humaxdigital.automotive.ximalaya");
            mXimalaya.init(this, mAppSecret);
        } else {
            String mAppSecret = "0a09d7093bff3d4947a5c4da0125972e";
            mXimalaya.setAppkey("f4d8f65918d9878e1702d49a8cdf0183");
            mXimalaya.setPackid("com.ximalaya.qunfeng");
            mXimalaya.init(this, mAppSecret);
        }
        XmPlayerManager.getInstance(this).init();//初始化喜马拉雅播放器
        sHandler = new Handler();
        sContext = getBaseContext();
    }

    /**
     * 获得主线程Handler，可以去更新UI
     */
    public static Handler getHandler() {
        return sHandler;
    }

    /**
     * 获取AppContext
     */
    public static Context getAppContext() {
        return sContext;
    }
}
