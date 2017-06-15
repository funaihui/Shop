package com.wizardev.shop.adapter;

import android.content.Context;

import com.customview.xiaohui.shop.R;
import com.customview.xiaohui.shop.bean.CategoryList;

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
        viewHolder.getTextView(R.id.left_list).setText(item.getName());
    }
}
