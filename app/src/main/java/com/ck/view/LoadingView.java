package com.ck.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ck.R;

public class LoadingView extends androidx.appcompat.widget.AppCompatImageView {

    private int mRotateDegree = 0;
    private boolean mNeedRotate = false;

    public LoadingView(@NonNull Context context) {
        this(context, null, 0);
    }

    public LoadingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setImageResource(R.mipmap.loading);//设置图标
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.rotate(mRotateDegree, getWidth() / 2, getHeight() / 2);//旋转角度，旋转点xy
        super.onDraw(canvas);
    }

    /**
     * 当绑定到window的时候
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mNeedRotate = true;
        /*post切换到主线程*/
        post(new Runnable() {
            @Override
            public void run() {
                mRotateDegree += 30;
                mRotateDegree = mRotateDegree >= 360 ? 0 : mRotateDegree;
                invalidate();
                if (mNeedRotate) {
                    postDelayed(this, 50);//延迟50毫秒旋转
                }
            }
        });
    }

    /**
     * 解绑window的时候
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mNeedRotate = false;
    }
}
