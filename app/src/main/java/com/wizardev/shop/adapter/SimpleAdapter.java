package com.wizardev.shop.adapter;

import android.content.Context;

import java.util.List;

/**
 * Created by wizardev on 17-6-1.
 */

public abstract class SimpleAdapter<T> extends BaseAdapter<T,BaseViewHolder> {


    public SimpleAdapter(Context mContext, List<T> list, int mResId) {
        super(mContext, list, mResId);
    }
}
