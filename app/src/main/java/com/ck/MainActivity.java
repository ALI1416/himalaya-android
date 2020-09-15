package com.ck;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.ck.adapter.IndicatorAdapter;
import com.ck.adapter.MainContentAdapter;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;


public class MainActivity extends FragmentActivity {
    private static final String TAG = "MainActivity";
    private final MainActivity _this = this;

    private MagicIndicator mMagicIndicator;
    private ViewPager mContentPager;

    private void log(String msg) {
        Log.d(TAG, msg);
    }

    private void toast(String msg) {
        Toast.makeText(_this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        //导航栏indicator
        mMagicIndicator = findViewById(R.id.indicator);
        mMagicIndicator.setBackgroundColor(getResources().getColor(R.color.colorMain));
        //创建导航栏indicator适配器
        IndicatorAdapter indicatorAdapter = new IndicatorAdapter(this);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(indicatorAdapter);
        //展示页面ViewPager
        mContentPager = findViewById(R.id.content_pager);
        //创建内容适配器
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        MainContentAdapter mainContentAdapter = new MainContentAdapter(supportFragmentManager);
        mContentPager.setAdapter(mainContentAdapter);
        //把ViewPager和indicator绑定到一起
        mMagicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mMagicIndicator, mContentPager);
    }
}