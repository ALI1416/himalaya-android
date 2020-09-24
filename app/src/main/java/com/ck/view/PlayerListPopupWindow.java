package com.ck.view;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.ck.R;
import com.ck.base.BaseApplication;

public class PlayerListPopupWindow extends PopupWindow {
    public PlayerListPopupWindow() {
        //设置宽高
        super(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置外部点击后关闭弹窗，一定要先设置setBackgroundDrawable
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//这个是圆角填充的颜色，不是外部的颜色
        setOutsideTouchable(true);
        //设置动画
        setAnimationStyle(R.style.popup_animation);
        //设置view
        View view = LayoutInflater.from(BaseApplication.getAppContext()).inflate(R.layout.popup_player_list, null);
        setContentView(view);
    }
}
