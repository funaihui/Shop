package com.wizardev.shop.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wizardev.shop.R;
import com.wizardev.shop.bean.Wares;
import com.wizardev.shop.dao.CartDao;

import java.util.List;

/**
 * Created by wizardev on 17-6-1.
 */

public  class HWAdapter extends SimpleAdapter<Wares> {
    private Context context;
    private CartDao cartDao;
    public HWAdapter(Context mContext, List<Wares> list, int mResId) {
        super(mContext, list, mResId);
        this.context = mContext;
        cartDao = new CartDao();
    }

    @Override
    public void bindView(BaseViewHolder viewHolder, final Wares item) {
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) viewHolder.getView(R.id.drawee_view);
        simpleDraweeView.setImageURI(item.getImgUrl());
        viewHolder.getTextView(R.id.text_title).setText(item.getName());
        viewHolder.getTextView(R.id.text_price).setText("￥" + item.getPrice());
        viewHolder.getView(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartDao.add2Cart(item);
                Toast.makeText(context,"添加购物车成功",Toast.LENGTH_SHORT).show();
            }
        });
    }


}
