package com.ck.util;

import com.ck.base.BaseFragment;
import com.ck.fragment.HistoryFragment;
import com.ck.fragment.RecommendFragment;
import com.ck.fragment.SubscriptionFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * 碎片放入缓存
 */
public class FragmentCreator {

    /*碎片序号*/
    public static final int INDEX_RECOMMEND = 0;
    public static final int INDEX_SUBSCRIPTION = 1;
    public static final int INDEX_HISTORY = 2;
    /*碎片个数*/
    public static final int PAGE_COUNT = 3;
    /*碎片存储器*/
    private static Map<Integer, BaseFragment> sCache = new HashMap<>();

    /*获取碎片*/
    public static BaseFragment getFragment(int index) {
        BaseFragment baseFragment = sCache.get(index);
        if (baseFragment != null) {
            return baseFragment;
        }
        switch (index) {
            case INDEX_RECOMMEND: {
                baseFragment = new RecommendFragment();
                break;
            }
            case INDEX_SUBSCRIPTION: {
                baseFragment = new SubscriptionFragment();
                break;
            }
            case INDEX_HISTORY: {
                baseFragment = new HistoryFragment();
                break;
            }
        }
        sCache.put(index, baseFragment);
        return baseFragment;
    }
}
