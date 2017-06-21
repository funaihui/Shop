package com.wizardev.shop.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.wizardev.shop.Contants;
import com.wizardev.shop.R;
import com.wizardev.shop.adapter.BaseAdapter;
import com.wizardev.shop.adapter.CategoryAdapter;
import com.wizardev.shop.adapter.WaresAdapter;
import com.wizardev.shop.adapter.decoration.DividerGridItemDecoration;
import com.wizardev.shop.adapter.decoration.DividerItemDecoration;
import com.wizardev.shop.bean.Banner;
import com.wizardev.shop.bean.CategoryList;
import com.wizardev.shop.bean.Page;
import com.wizardev.shop.bean.Wares;
import com.wizardev.shop.customView.MyToolbar;
import com.wizardev.shop.http.OkHttpHelper;
import com.wizardev.shop.http.SpotsCallback;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import okhttp3.Response;


public class CategoryFragment extends Fragment {
    @ViewInject(R.id.slider)
    private SliderLayout mSliderLayout;
    @ViewInject(R.id.left_list)
    private RecyclerView mListRecyclerView;
    @ViewInject(R.id.drawer_layout)
    private DrawerLayout mDrawerLayout;
    @ViewInject(R.id.mytoolbar)
    private MyToolbar toolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private List<Banner> mBanner;
    private List<CategoryList> mLeftDatas;
    private OkHttpHelper httpHelper = OkHttpHelper.getInstance();
    private static final String TAG = "CategoryFragment";
    private int category_id = 1;
    private int currPage = 1;
    private int totalPage = 1;
    private int pageSize = 10;
    private WaresAdapter mWaresAdatper;
    @ViewInject(R.id.recyclerView_category)
    private RecyclerView mRecyclerviewWares;
    @ViewInject(R.id.refresh_category)
    private MaterialRefreshLayout mRefreshLaout;

    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFREH = 1;
    private static final int STATE_MORE = 2;

    private int state = STATE_NORMAL;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        x.view().inject(this, view);
        requestImage();
        initDrawerToggle();
        getLeftListData();
        initRefreshLayout();
        return view;
    }

    private void requestWares(final int categoryId) {
        String url = Contants.API.WARES_LIST + "?categoryId=" + categoryId + "&curPage=" + currPage + "&pageSize=" + pageSize;
        httpHelper.get(url, new SpotsCallback<Page<Wares>>(getContext()) {
            @Override
            public void onSuccess(Response response, Page<Wares> waresPage) {
                currPage = waresPage.getCurrentPage();
                totalPage = waresPage.getTotalPage();

                showWaresData(waresPage.getList());
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

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
//                    Toast.makeText()
                    mRefreshLaout.finishRefreshLoadMore();
                }
            }
        });
    }


    private void refreshData() {

        currPage = 1;

        state = STATE_REFREH;
        requestWares(category_id);

    }

    private void loadMoreData() {

        currPage = ++currPage;
        state = STATE_MORE;

        requestWares(category_id);

    }

    private void showWaresData(List<Wares> list) {
        switch (state) {

            case STATE_NORMAL:

                if (mWaresAdatper == null) {
                    mWaresAdatper = new WaresAdapter(getContext(), list,R.layout.category_item);

                    mRecyclerviewWares.setAdapter(mWaresAdatper);
                    mRecyclerviewWares.setHasFixedSize(true);
                    mRecyclerviewWares.setLayoutManager(new GridLayoutManager(getContext(), 2));
                    mRecyclerviewWares.setItemAnimator(new DefaultItemAnimator());
                    mRecyclerviewWares.addItemDecoration(new DividerGridItemDecoration(getContext()));
                } else {
                    mWaresAdatper.cleanData();
                    mWaresAdatper.addData(list);
                }


                break;

            case STATE_REFREH:
                mWaresAdatper.cleanData();
                mWaresAdatper.addData(list);

                mRecyclerviewWares.scrollToPosition(0);
                mRefreshLaout.finishRefresh();
                break;

            case STATE_MORE:
                mWaresAdatper.addData(mWaresAdatper.getDatas().size(), list);
                mRecyclerviewWares.scrollToPosition(mWaresAdatper.getDatas().size());
                mRefreshLaout.finishRefreshLoadMore();
                break;


        }

    }

    private void initDrawerToggle() {
        toolbar.setNavigationIcon(R.drawable.ic_menu_write_24dp);
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout, toolbar,
                R.string.catagory, R.string.catagory
        );

        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }

    public void requestImage() {
        String url = "http://112.124.22.238:8081/course_api/banner/query?type=1";
        httpHelper.get(url, new SpotsCallback<List<Banner>>(this.getActivity()) {
            @Override
            public void onSuccess(Response response, List<Banner> banners) {
                mBanner = banners;
                initSlider();
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    public void initSlider() {
        if (mBanner != null) {
            for (Banner banner : mBanner) {
                TextSliderView textSliderView = new TextSliderView(this.getActivity());
                textSliderView.image(banner.getImgUrl());
                textSliderView.description(banner.getName());
                textSliderView.setScaleType(BaseSliderView.ScaleType.Fit);
                mSliderLayout.addSlider(textSliderView);
            }
        }
        mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mSliderLayout.setCustomAnimation(new DescriptionAnimation());
        mSliderLayout.setPresetTransformer(SliderLayout.Transformer.RotateUp);
        mSliderLayout.setDuration(3000);
    }

    public void getLeftListData() {
        httpHelper.get(Contants.API.CATEGORY_LIST, new SpotsCallback<List<CategoryList>>(getContext()) {
            @Override
            public void onSuccess(Response response, List<CategoryList> leftDatas) {
                mLeftDatas = leftDatas;
                if(leftDatas !=null && leftDatas.size()>0)
                    category_id = leftDatas.get(0).getId();
                requestWares(category_id);
                showLeftList();
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    private void showLeftList() {
        final CategoryAdapter adapter = new CategoryAdapter(getContext(), mLeftDatas, R.layout.left_list_item);
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                CategoryList item = adapter.getItem(position);
                requestWares(item.getId());
                mDrawerLayout.closeDrawer(mListRecyclerView);
            }
        });
        mListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mListRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mListRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        mListRecyclerView.setAdapter(adapter);
    }

}



