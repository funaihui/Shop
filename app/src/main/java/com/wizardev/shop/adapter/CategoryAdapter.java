package com.wizardev.shop.adapter;

import android.content.Context;

import com.wizardev.shop.R;
import com.wizardev.shop.bean.CategoryList;

import java.util.List;

/**
 * Created by wizardev on 17-6-2.
 */

public class CategoryAdapter extends SimpleAdapter<CategoryList> {
    public CategoryAdapter(Context mContext, List<CategoryList> list, int mResId) {
        super(mContext, list, mResId);
    }

    @Override
    public void bindView(BaseViewHolder viewHolder, CategoryList item) {
        viewHolder.getTextView(R.id.left_list_text).setText(item.getName());
    }
}
