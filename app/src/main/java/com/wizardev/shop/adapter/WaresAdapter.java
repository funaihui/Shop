package com.wizardev.shop.adapter;

import android.content.Context;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wizardev.shop.R;
import com.wizardev.shop.bean.Wares;

import java.util.List;

/**
 * Created by wizardev on 17-6-3.
 */

public class WaresAdapter extends SimpleAdapter<Wares> {
    public WaresAdapter(Context mContext, List<Wares> list, int mResId) {
        super(mContext, list, mResId);
    }

    @Override
    public void bindView(BaseViewHolder viewHolder, Wares item) {
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) viewHolder.getView(R.id.drawee_view);
        simpleDraweeView.setImageURI(item.getImgUrl());
        viewHolder.getTextView(R.id.text_title).setText(item.getName());
        viewHolder.getTextView(R.id.text_price).setText("￥" + item.getPrice());
    }
}
