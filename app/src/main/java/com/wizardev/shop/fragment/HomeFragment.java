package com.wizardev.shop.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.gson.Gson;
import com.wizardev.shop.R;
import com.wizardev.shop.adapter.DivideItemDecoration;
import com.wizardev.shop.adapter.HomeCategoryAdapter;
import com.wizardev.shop.bean.Banner;
import com.wizardev.shop.bean.Campaign;
import com.wizardev.shop.bean.HomeCampaign;
import com.wizardev.shop.bean.HomeCategoryBean;
import com.wizardev.shop.http.BaseCallback;
import com.wizardev.shop.http.OkHttpHelper;
import com.wizardev.shop.http.SpotsCallback;
import com.wizardev.shop.widget.Contants;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;
import okhttp3.Response;


public class HomeFragment  extends Fragment {
    private SliderLayout mSliderLayout;
    private RecyclerView mRecyclerView;
    private List<HomeCategoryBean> mList = new ArrayList<>();
    private HomeCategoryAdapter adapter;
    private Gson mGson = new Gson();
    private List<Banner> mBanner;
    private OkHttpHelper httpHelper = OkHttpHelper.getInstance();
    private static final String TAG = "WizardFu";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        mSliderLayout = (SliderLayout) view.findViewById(R.id.slider);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        requestImage();
        initRecyclerView();
        return view;
    }
    private void initRecyclerView() {
        httpHelper.get(Contants.API.CAMPAIGN_HOME, new BaseCallback<List<HomeCampaign>>() {
            @Override
            public void onBeforeCallback(Request request) {

            }

            @Override
            public void onFailure(Request request, Exception e) {

            }

            @Override
            public void onSuccess(Response response, List<HomeCampaign> homeCampaigns) {
                initData(getActivity(),homeCampaigns);
            }


            @Override
            public void onError(Response response, int code, Exception e) {

            }

            @Override
            public void onResponse(Response response) {

            }
        });
    }

    private void initData(FragmentActivity activity, List<HomeCampaign> homeCampaigns) {
        adapter = new HomeCategoryAdapter(homeCampaigns,activity);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mRecyclerView.addItemDecoration(new DivideItemDecoration());
        adapter.setOnItemClickListener(new HomeCategoryAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, Campaign campaign) {
                Toast.makeText(getActivity(),""+campaign.getTitle(),Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void requestImage(){
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
    public void initSlider(){
        Log.i(TAG, "initSlider: ");
        if (mBanner != null){
            for (Banner banner : mBanner){
                TextSliderView textSliderView = new TextSliderView(this.getActivity());
                String name = banner.getName();
                String imageUrl = banner.getImgUrl();
                textSliderView.description(name).image(imageUrl);
                mSliderLayout.addSlider(textSliderView);
            }
        }
        mSliderLayout.setDuration(3000);
    }
}