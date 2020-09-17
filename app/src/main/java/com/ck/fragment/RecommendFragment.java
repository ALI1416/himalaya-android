package com.ck.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ck.R;
import com.ck.adapter.RecommendListAdapter;
import com.ck.base.BaseFragment;
import com.ck.interfaces.IRecommendViewCallback;
import com.ck.presenter.RecommendPresenter;
import com.ck.util.L;
import com.ck.view.UILoader;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;

public class RecommendFragment extends BaseFragment implements IRecommendViewCallback {
    private View mRootView;
    private RecyclerView mRecommendRecyclerView;
    private RecommendListAdapter mRecommendListAdapter;
    private RecommendPresenter mRecommendPresenter;
    private UILoader mUILoader;

    @Override
    protected View onSubViewLoaded(final LayoutInflater layoutInflater, ViewGroup container) {
        mUILoader = new UILoader(getContext()) {
            @Override
            protected View getSuccessView(ViewGroup container) {
                return createSuccessView(layoutInflater, container);
            }
        };
        //获取到逻辑层的对象
        mRecommendPresenter = RecommendPresenter.getInstance();
        //设置通知接口的注册
        mRecommendPresenter.registerViewCallback(this);
        //获取推荐列表
        mRecommendPresenter.getRecommendList();
        //与父类解绑，不能重复绑定View
        if (mUILoader.getParent() instanceof ViewGroup) {
            ((ViewGroup) mUILoader.getParent()).removeView(mUILoader);//父类解绑自己
        }
        return mUILoader;
    }

    private View createSuccessView(LayoutInflater layoutInflater, ViewGroup container) {
        //布局
        mRootView = layoutInflater.inflate(R.layout.fragment_recommend, container, false);
        //列表元素
        mRecommendRecyclerView = mRootView.findViewById(R.id.recommend_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecommendRecyclerView.setLayoutManager(linearLayoutManager);
        /*偏移已经在layout里设置过了*/
//        mRecommendRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
//            /*getItemOffsets设置layout元素偏移*/
//            @Override
//            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
//                /*UIUtil.dip2px工具类，dp2px,px2dp*/
//                outRect.top = UIUtil.dip2px(view.getContext(), 5);
//                outRect.bottom = UIUtil.dip2px(view.getContext(), 5);
//                outRect.left = UIUtil.dip2px(view.getContext(), 5);
//                outRect.right = UIUtil.dip2px(view.getContext(), 5);
//            }
//        });
        //适配器
        mRecommendListAdapter = new RecommendListAdapter();
        mRecommendRecyclerView.setAdapter(mRecommendListAdapter);
        return mRootView;
    }

    @Override
    public void onRecommendListLoaded(List<Album> result) {
        //获取到推荐内容时，这个方法就被调用了，去更新UI
        mRecommendListAdapter.setData(result);
        mUILoader.updateStatus(UILoader.UIStatus.SUCCESS);
    }

    @Override
    public void networkError() {
        mUILoader.updateStatus(UILoader.UIStatus.NOTWORK_ERROR);
    }

    @Override
    public void onEmpty() {
        mUILoader.updateStatus(UILoader.UIStatus.EMPTY);
    }

    @Override
    public void onLoading() {
        mUILoader.updateStatus(UILoader.UIStatus.LOADING);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //取消接口注册，避免内存泄露
        if (mRecommendPresenter != null) {
            mRecommendPresenter.unRegisterViewCallback(this);
        }
    }
}
