package com.wizardev.shop.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wizardev.shop.R;
import com.wizardev.shop.bean.Campaign;
import com.wizardev.shop.bean.HomeCampaign;

import java.util.List;

/**
 * Created by xiaohui on 2016/10/28.
 */

public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.ViewHolder>{
    private  static int VIEW_TYPE_L=0;
    private  static int VIEW_TYPE_R=1;
    private LayoutInflater mInflater;
    private List<HomeCampaign> mList;
    private Context mContext;
    private  OnItemClickListener mListener;
    public HomeCategoryAdapter(List<HomeCampaign> list,Context context) {
        mList = list;
        mContext = context;
    }

    public  void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(parent.getContext());
        if (viewType ==VIEW_TYPE_L){
            return new ViewHolder(mInflater.inflate(R.layout.home_cardview2,null));
        }
        return new ViewHolder(mInflater.inflate(R.layout.home_cardview,null));

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textTitle.setText(mList.get(position).getTitle());

        Picasso.with(mContext).load(mList.get(position).getCpOne().getImgUrl()).into(holder.imageBig);
        Picasso.with(mContext).load(mList.get(position).getCpTwo().getImgUrl()).into(holder.imageSmallTop);
        Picasso.with(mContext).load(mList.get(position).getCpThree().getImgUrl()).into(holder.imageSmallBottom);

    }

    @Override
    public int getItemViewType(int position) {
        if(position % 2==0){
            return  VIEW_TYPE_R;
        }
        else return VIEW_TYPE_L;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

     class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textTitle;
        private ImageView imageBig;
        private ImageView imageSmallTop;
        private ImageView imageSmallBottom;
        public ViewHolder(View itemView) {
            super(itemView);
            textTitle = (TextView) itemView.findViewById(R.id.text_title);
            imageBig = (ImageView) itemView.findViewById(R.id.img_view_big);
            imageSmallTop = (ImageView) itemView.findViewById(R.id.img_view_small_top);
            imageSmallBottom = (ImageView) itemView.findViewById(R.id.img_view_small_bottom);

            imageBig.setOnClickListener(this);
            imageSmallTop.setOnClickListener(this);
            imageSmallBottom.setOnClickListener(this);
        }


         @Override
         public void onClick(View v) {
             HomeCampaign campaign = mList.get(getLayoutPosition());
             if (mListener != null){
                 anim(v,campaign);
             }

         }
     }
    private  void anim(final View v, final HomeCampaign campaign){
        ObjectAnimator animator =  ObjectAnimator.ofFloat(v, "rotation", 0.0F, 360.0F)
                .setDuration(200);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                switch (v.getId()){

                    case  R.id.img_view_big:
                        mListener.onClick(v, campaign.getCpOne());
                        break;

                    case  R.id.img_view_small_top:
                        mListener.onClick(v, campaign.getCpTwo());
                        break;

                    case R.id.img_view_small_bottom:
                        mListener.onClick(v,campaign.getCpThree());
                        break;
                }
            }
        });
        animator.setInterpolator(new AnticipateInterpolator());
        animator.start();
    }
    public interface OnItemClickListener{
        void onClick(View view, Campaign campaign);
    }
}
