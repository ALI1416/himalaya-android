package com.ck.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ck.R;
import com.ck.base.BaseFragment;

public class HistoryFragment extends BaseFragment {

    private View rootView;

    @Override
    protected View onSubViewLoaded(LayoutInflater layoutInflater, ViewGroup container) {
        rootView = layoutInflater.inflate(R.layout.fragment_history, container, false);
        return rootView;
    }
}
