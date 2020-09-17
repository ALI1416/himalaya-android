package com.ck.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ck.R;
import com.ck.base.BaseApplication;

public abstract class UILoader extends FrameLayout {

    private View mLoadingView;
    private View mSuccessView;
    private View mEmptyView;
    private View mNetworkView;

    /**
     * UI页面的几种状态
     */
    public enum UIStatus {
        LOADING, SUCCESS, NOTWORK_ERROR, EMPTY, NONE
    }

    /**
     * 当前状态
     */
    public UIStatus mCurrentStatus = UIStatus.NONE;

    public UILoader(@NonNull Context context) {
        this(context, null);
    }

    public UILoader(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UILoader(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void updateStatus(UIStatus status) {
        mCurrentStatus = status;
        //去主线程更新UI
        BaseApplication.getHandler().post(new Runnable() {
            @Override
            public void run() {
                switchUIByCurrentStatus();
            }
        });
    }

    /**
     * 初始化UI界面
     */
    private void init() {
        switchUIByCurrentStatus();
    }

    /**
     * 从当前状态中选择加载的页面
     */
    private void switchUIByCurrentStatus() {
        //加载中
        if (mLoadingView == null) {
            mLoadingView = getLoadingView();
            addView(mLoadingView);
        }
        mLoadingView.setVisibility(mCurrentStatus == UIStatus.LOADING ? VISIBLE : GONE); //设置可见性
        //成功
        if (mSuccessView == null) {
            mSuccessView = getSuccessView(this);
            addView(mSuccessView);
        }
        mSuccessView.setVisibility(mCurrentStatus == UIStatus.SUCCESS ? VISIBLE : GONE);
        //数据为空
        if (mEmptyView == null) {
            mEmptyView = getEmptyView();
            addView(mEmptyView);
        }
        mEmptyView.setVisibility(mCurrentStatus == UIStatus.EMPTY ? VISIBLE : GONE);
        //网络异常
        if (mNetworkView == null) {
            mNetworkView = getNetworkErrorView();
            addView(mNetworkView);
        }
        mNetworkView.setVisibility(mCurrentStatus == UIStatus.NOTWORK_ERROR ? VISIBLE : GONE);
    }

    private View getLoadingView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_loading_view, this, false);
    }

    protected abstract View getSuccessView(ViewGroup container);

    private View getEmptyView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_empty_view, this, false);
    }

    private View getNetworkErrorView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_network_error_view, this, false);
    }
}
