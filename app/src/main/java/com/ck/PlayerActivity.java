package com.ck;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.ck.base.BaseActivity;
import com.ck.presenter.PlayerPresenter;

public class PlayerActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        PlayerPresenter playerPresenter = PlayerPresenter.getInstance();
        playerPresenter.play();
    }
}
