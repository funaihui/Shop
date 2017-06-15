package com.wizardev.shop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by wizardev on 17-6-1.
 */

public abstract class BaseAdapter<T, H extends BaseViewHolder> extends RecyclerView.Adapter<BaseViewHolder> {
    private Context mContext;
    private List<T> list;
    private int mResId;
    private LayoutInflater layoutInflater;
    private OnItemClickListener clickListener;

    public BaseAdapter(Context mContext, List<T> list, int mResId) {
        this.mContext = mContext;
        this.list = list;
        this.mResId = mResId;
        layoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder viewHolder = new BaseViewHolder(layoutInflater.inflate(mResId, parent, false),
                clickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        T item = getItem(position);
        bindView((H) holder,item);

    }
    public void cleanData() {
        int itemCount = list.size();
        list.clear();
        this.notifyItemRangeRemoved(0, itemCount);
    }

    public void addData(List<T> wares) {
        addData(0, wares);
    }

    public List<T> getDatas() {
        return list;
    }

    public void addData(int position, List<T> wares) {

        if (wares != null && wares.size() > 0) {
            list.addAll(wares);
            this.notifyItemRangeChanged(position, wares.size());
        }

    }
    public T getItem(int position) {
        if (position>list.size()) return null;
        return list.get(position);
    }

    public abstract void bindView(H viewHolder,T item);

    @Override
    public int getItemCount() {
        if(list!=null && list.size()>0)
            return list.size();

        return 0;
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.clickListener = listener;
    }
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
