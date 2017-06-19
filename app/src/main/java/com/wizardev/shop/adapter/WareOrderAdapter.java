package com.wizardev.shop.adapter;

import android.content.Context;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wizardev.shop.R;
import com.wizardev.shop.bean.Cart;

import java.util.List;

/**
 * Created by wizardev on 17-6-19.
 */

public class WareOrderAdapter extends SimpleAdapter<Cart>{
    private List<Cart> list;
    public WareOrderAdapter(Context mContext, List<Cart> list, int mResId) {
        super(mContext, list, mResId);
        this.list = list;
    }

    @Override
    public void bindView(BaseViewHolder viewHolder, Cart item) {
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) viewHolder.getView(R.id.drawee_view);
        simpleDraweeView.setImageURI(item.getImgUrl());
    }

    public float totalPrice(){
        float sum = 0;
        if (list!=null&&!list.isEmpty()){
            for (Cart c : list) {
                sum+=c.getPrice()*c.getCount();
            }
        }
        return sum;
    }
}
