package com.ck.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ck.R;
import com.ck.adapter.RecommendListAdapter;
import com.ck.base.BaseFragment;
import com.ck.constant.RecommendConstant;
import com.ck.util.L;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecommendFragment extends BaseFragment {
    private View mRootView;
    private RecyclerView mRecommendRecyclerView;
    private RecommendListAdapter mRecommendListAdapter;

    @Override
    protected View onSubViewLoaded(LayoutInflater layoutInflater, ViewGroup container) {
        //布局
        mRootView = layoutInflater.inflate(R.layout.fragment_recommend, container, false);
        //列表元素
        mRecommendRecyclerView = mRootView.findViewById(R.id.recommend_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecommendRecyclerView.setLayoutManager(linearLayoutManager);
        //适配器
        mRecommendListAdapter = new RecommendListAdapter();
        mRecommendRecyclerView.setAdapter(mRecommendListAdapter);
        //取数据
        getRecommendData2();
        return mRootView;
    }

    private void getRecommendData2() {
        List<Album> albumList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Album album = new Album();
            album.setAlbumTitle(i + "啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊");
            album.setAlbumIntro(i + "哈哈哈哈哈哈或或或或或哈哈哈哈哈哈啊哈哈哈");
            album.setPlayCount(i);
            album.setIncludeTrackCount(20 - i);
            album.setCoverUrlLarge("https://himg.bdimg.com/sys/portrait/item/pp.1.a6246271.hPNvzMm7d_71DIWDOusXCw?_t=1600244882165");
            albumList.add(album);
        }
        updateRecommendUI(albumList);
    }

    /**
     * 获取推荐内容（猜你喜欢）
     */
    private void getRecommendData() {
        Map<String, String> map = new HashMap<>();
        //一页数据返回条数
        map.put(DTransferConstants.LIKE_COUNT, String.valueOf(RecommendConstant.RECOMMEND_COUNT));
        CommonRequest.getGuessLikeAlbum(map, new IDataCallBack<GussLikeAlbumList>() {
            @Override
            public void onSuccess(GussLikeAlbumList gussLikeAlbumList) {
                if (gussLikeAlbumList != null) {
                    List<Album> albumList = gussLikeAlbumList.getAlbumList();
                    updateRecommendUI(albumList);
                    for (Album album : albumList) {
                        L.d(album.toString());
                    }
                }
            }

            @Override
            public void onError(int i, String s) {
                L.e("errorCode-->" + i);
                L.e("errorMsg-->" + s);
            }
        });
    }

    private void updateRecommendUI(List<Album> albumList) {
        mRecommendListAdapter.setData(albumList);
    }

}
