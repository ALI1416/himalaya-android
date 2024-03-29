package com.ck.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.ck.R;
import com.ck.adapter.BaseRecyclerAdapter;
import com.ck.adapter.SmartViewHolder;
import com.ck.base.BaseFragment;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import static android.R.layout.simple_list_item_2;
import static androidx.recyclerview.widget.DividerItemDecoration.VERTICAL;

public class SubscriptionFragment extends BaseFragment {
    private View rootView;
    private BaseRecyclerAdapter<Void> mAdapter;
    private Random random = new Random();

    @Override
    protected View onSubViewLoaded(LayoutInflater layoutInflater, ViewGroup container) {
        rootView = layoutInflater.inflate(R.layout.fragment_subscription, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);

        final Toolbar toolbar = root.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        if (recyclerView != null) {
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), VERTICAL));
            recyclerView.setAdapter(mAdapter = new BaseRecyclerAdapter<Void>(simple_list_item_2) {
                @Override
                protected void onBindViewHolder(SmartViewHolder holder, Void model, int position) {
                    holder.text(android.R.id.text1, "标题" + position);
                    holder.text(android.R.id.text2, "内容" + position);
                    holder.textColorId(android.R.id.text2, R.color.colorAccent);
                }
            });
        }

        RefreshLayout refreshLayout = root.findViewById(R.id.refreshLayout);
        if (refreshLayout != null) {
            refreshLayout.autoRefresh();
            refreshLayout.setEnableLoadMoreWhenContentNotFull(false);
            refreshLayout.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                    refreshLayout.getLayout().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Collection<Void> data = loadData();
                            mAdapter.refresh(data);
                            if (data.size() < 9) {
                                refreshLayout.finishRefreshWithNoMoreData();
                            } else {
                                refreshLayout.finishRefresh();
                            }
                        }
                    }, 1000);
                }
            });
            refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                    refreshLayout.getLayout().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Collection<Void> list = loadData();
                            mAdapter.loadMore(list);
                            if (list.size() < 10) {
                                refreshLayout.finishLoadMoreWithNoMoreData();
                            } else {
                                refreshLayout.finishLoadMore();
                            }
                        }
                    }, 1000);
                }
            });
        }
    }

    private Collection<Void> loadData() {
        int count = 3 + random.nextInt(10);
        List<Void> list = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            list.add(null);
        }
        return list;
    }

}
