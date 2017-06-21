package com.wizardev.shop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.wizardev.shop.Contants;
import com.wizardev.shop.R;
import com.wizardev.shop.WaresDetailActivity;
import com.wizardev.shop.adapter.BaseAdapter;
import com.wizardev.shop.adapter.HWAdapter;
import com.wizardev.shop.adapter.decoration.DividerItemDecoration;
import com.wizardev.shop.bean.Page;
import com.wizardev.shop.bean.Wares;
import com.wizardev.shop.http.OkHttpHelper;
import com.wizardev.shop.http.SpotsCallback;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import okhttp3.Response;


public class HotFragment extends Fragment {
    private OkHttpHelper httpHelper = OkHttpHelper.getInstance();
    private int currPage = 1;
    private int totalPage = 1;
    private int pageSize = 10;

    private List<Wares> datas;
    private static final String TAG = "HotFragment";
    private HWAdapter mAdatper;
    @ViewInject(R.id.recyclerview)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.refresh_view)
    private MaterialRefreshLayout mRefreshLaout;


    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFREH = 1;
    private static final int STATE_MORE = 2;

    private int state = STATE_NORMAL;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_hot, container, false);
        x.view().inject(this, view);
        initRefreshLayout();
        getData();
        return view;

    }


    private void initRefreshLayout() {
        mRefreshLaout.setLoadMore(true);
        mRefreshLaout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                refreshData();

            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {

                if (currPage <= totalPage)
                    loadMoreData();
                else {
                    Toast.makeText(getContext(),"已经到底了",Toast.LENGTH_SHORT).show();
                    mRefreshLaout.finishRefreshLoadMore();
                }
            }
        });
    }


    private void refreshData() {

        currPage = 1;

        state = STATE_REFREH;
        getData();

    }

    private void loadMoreData() {

        currPage = ++currPage;
        state = STATE_MORE;

        getData();

    }


    private void getData() {


        String url = Contants.API.WARES_HOT + "?curPage=" + currPage + "&pageSize=" + pageSize;
        Log.i(TAG, "url: " + url);
        httpHelper.get(url, new SpotsCallback<Page<Wares>>(getContext()) {


            @Override
            public void onSuccess(Response response, Page<Wares> waresPage) {


                datas = waresPage.getList();
                currPage = waresPage.getCurrentPage();
                totalPage = waresPage.getTotalPage();

                showData();
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }

            @Override
            public void onTokenError(Response response, int code) {

            }
        });


    }


    private void showData() {


        switch (state) {

            case STATE_NORMAL:
                mAdatper = new HWAdapter(getContext(), datas, R.layout.template_hot_wares);


                mRecyclerView.setAdapter(mAdatper);
                mAdatper.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Wares wares = mAdatper.getItem(position);
                        Intent intent = new Intent(getActivity(), WaresDetailActivity.class);
                        intent.putExtra(Contants.WARES,wares);
                        startActivity(intent);
                    }
                });
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));


                break;

            case STATE_REFREH:
                mAdatper.cleanData();
                mAdatper.addData(datas);

                mRecyclerView.scrollToPosition(0);
                mRefreshLaout.finishRefresh();
                break;

            case STATE_MORE:
                mAdatper.addData(mAdatper.getDatas().size(), datas);
                mRecyclerView.scrollToPosition(mAdatper.getDatas().size());
                mRefreshLaout.finishRefreshLoadMore();
                break;


        }


    }
}